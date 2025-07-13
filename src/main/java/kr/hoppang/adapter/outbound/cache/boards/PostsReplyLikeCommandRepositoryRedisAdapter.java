package kr.hoppang.adapter.outbound.cache.boards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository("PostsReplyLikeCommandRepositoryRedis")
@RequiredArgsConstructor
public class PostsReplyLikeCommandRepositoryRedisAdapter
        implements PostsReplyLikeCommandRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public static final String POSTS_REPLY_LIKE_COUNT_CACHE_KEY_PREFIX = "reply:likes:count:{replyId}";
    public static final String POSTS_REPLY_LIKE_BUFFER_CACHE_KEY_PREFIX = "reply:likes:buffer:{replyId}";


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.CACHE;
    }

    @Override
    public void create(final PostsReplyLike postsReplyLike) {
        final SetOperations<String, String> setOps = redisTemplate.opsForSet();
        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String likeInfoKey = POSTS_REPLY_LIKE_BUFFER_CACHE_KEY_PREFIX.replace(
                "{replyId}",
                postsReplyLike.postReplyId().toString()
        );

        String countKey = POSTS_REPLY_LIKE_COUNT_CACHE_KEY_PREFIX.replace(
                "{replyId}",
                postsReplyLike.postReplyId().toString()
        );

        try {
            String redisValue = objectMapper.writeValueAsString(
                    Map.of(
                            "userId", postsReplyLike.userId(),
                            "clickedAt", postsReplyLike.clickedAt()
                    )
            );

            Long added = setOps.add(likeInfoKey, redisValue);

            if (added != null && added == 1L) {

                String script = """
                                    local newValue = redis.call('INCR', KEYS[1])
                                    if newValue == 1 then
                                        redis.call('EXPIRE', KEYS[1], tonumber(ARGV[1]))
                                    end
                                    return newValue
                                """;

                redisTemplate.execute(
                        RedisScript.of(script, Long.class),
                        List.of(countKey),
                        Duration.ofMinutes(30).getSeconds()
                );
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis value 직렬화 실패", e);
        }
    }

    @Override
    public void createAll(final List<PostsReplyLike> postsReplyLikeList) {

    }

    @Override
    public void delete(final PostsReplyLike postsReplyLike) {
        final SetOperations<String, String> setOps = redisTemplate.opsForSet();
        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String likeInfoKey = POSTS_REPLY_LIKE_BUFFER_CACHE_KEY_PREFIX.replace(
                "{replyId}",
                postsReplyLike.postReplyId().toString()
        );

        String countKey = POSTS_REPLY_LIKE_COUNT_CACHE_KEY_PREFIX.replace(
                "{replyId}",
                postsReplyLike.postReplyId().toString()
        );

        try {
            boolean isAnyDeleted = false;

            Set<String> allMembers = setOps.members(likeInfoKey);

            if (allMembers != null) {
                Long removed = 0L;

                for (String member : allMembers) {
                    try {
                        JsonNode node = objectMapper.readTree(member);
                        if (node.get("userId").asLong() == postsReplyLike.userId()) {
                            removed = setOps.remove(likeInfoKey, member);
                            if (removed != null && removed > 0) {
                                isAnyDeleted = true;
                                break; // 첫 번째 일치 항목만 삭제하고 탈출
                            }
                        }
                    } catch (JsonProcessingException e) {
                        // 무시하고 다음 항목 계속 진행
                        log.warn("잘못된 JSON 형식: {}", member);
                    }
                }

                if (isAnyDeleted) {
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
            }

        } catch (Exception e) {
            throw new RuntimeException("Redis 삭제 중 오류 발생", e);
        }
    }
}
