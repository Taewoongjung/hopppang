package kr.hoppang.adapter.outbound.cache.boards.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository("PostsLikeCommandRepositoryRedis")
@RequiredArgsConstructor
public class PostsLikeCommandRepositoryRedisAdapter
        implements PostsLikeCommandRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public static final String POSTS_LIKE_COUNT_CACHE_KEY_PREFIX = "post:likes:count:{postId}";
    public static final String POSTS_LIKE_BUFFER_CACHE_KEY_PREFIX = "post:likes:buffer:{postId}";


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.CACHE;
    }

    @Override
    public void create(final PostsLike postsLike) {
        final SetOperations<String, String> setOps = redisTemplate.opsForSet();

        String likeInfoKey = POSTS_LIKE_BUFFER_CACHE_KEY_PREFIX.replace(
                "{postId}",
                postsLike.postId().toString()
        );

        String countKey = POSTS_LIKE_COUNT_CACHE_KEY_PREFIX.replace(
                "{postId}",
                postsLike.postId().toString()
        );

        try {
            String redisValue = objectMapper.writeValueAsString(
                    Map.of(
                            "userId", postsLike.userId(),
                            "clickedAt", postsLike.clickedAt()
                    )
            );

            setOps.add(likeInfoKey, redisValue);

            String script = """
                                local newValue = redis.call('INCR', KEYS[1])
                                redis.call('EXPIRE', KEYS[1], tonumber(ARGV[1]))
                                return newValue
                            """;

            redisTemplate.execute(
                    RedisScript.of(script, Long.class),
                    List.of(countKey),
                    String.valueOf(Duration.ofMinutes(10).getSeconds())
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis value 직렬화 실패", e);
        }
    }

    @Override
    public void createAll(final List<PostsLike> postsLikes) {
        // yet developed
    }

    @Override
    public void delete(final PostsLike postsLike) {
        final SetOperations<String, String> setOps = redisTemplate.opsForSet();

        String likeBufferKey = POSTS_LIKE_BUFFER_CACHE_KEY_PREFIX.replace(
                "{postId}",
                postsLike.postId().toString()
        );

        String countKey = POSTS_LIKE_COUNT_CACHE_KEY_PREFIX.replace(
                "{postId}",
                postsLike.postId().toString()
        );

        try {
            Set<String> allBuffers = setOps.members(likeBufferKey);

            if (allBuffers != null) {
                Long removed = 0L;

                for (String buffer : allBuffers) {
                    try {
                        JsonNode node = objectMapper.readTree(buffer);
                        if (node.get("userId").asLong() == postsLike.userId()) {
                            removed = setOps.remove(likeBufferKey, buffer);
                            if (removed != null && removed > 0) {
                                break; // 첫 번째 일치 항목만 삭제하고 탈출
                            }
                        }
                    } catch (JsonProcessingException e) {
                        // 무시하고 다음 항목 계속 진행
                        log.warn("잘못된 JSON 형식: {}", buffer);
                    }
                }

                String script = """
                                    local current = redis.call("GET", KEYS[1])
                                    if current and tonumber(current) > 0 then
                                        return redis.call("DECRBY", KEYS[1], ARGV[1])
                                    else
                                        return current
                                    end
                                """;

                redisTemplate.execute(
                        RedisScript.of(script, Long.class),
                        List.of(countKey),
                        "1"
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Redis 삭제 중 오류 발생", e);
        }
    }
}
