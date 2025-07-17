package kr.hoppang.adapter.outbound.cache.boards.readmodel;

import static kr.hoppang.adapter.outbound.cache.boards.command.PostsLikeCommandRepositoryRedisAdapter.POSTS_LIKE_BUFFER_CACHE_KEY_PREFIX;
import static kr.hoppang.adapter.outbound.cache.boards.command.PostsLikeCommandRepositoryRedisAdapter.POSTS_LIKE_COUNT_CACHE_KEY_PREFIX;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostsLikeRepositoryRedisAdapter implements PostsLikeQueryRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.CACHE;
    }

    @Override
    public Map<Long, Long> findCountOfLikesByPostIds(final List<Long> postIds) {
        Map<Long, Long> result = new HashMap<>();

        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        for (Long postId : postIds) {
            String key = POSTS_LIKE_COUNT_CACHE_KEY_PREFIX.replace(
                    "{postId}",
                    postId.toString()
            );

            String value = valueOps.get(key);

            if (value != null) {
                try {
                    Long count = Long.parseLong(value.trim());

                    result.put(postId, count);

                } catch (NumberFormatException ignored) {
                    // 잘못된 값이 들어가 있을 경우 예외 방지
                }
            }
        }

        return result;
    }

    @Override
    public Boolean isLikedByPostId(final Long postId, final Long loggedInUserId) {
        final SetOperations<String, String> setOps = redisTemplate.opsForSet();

        String key = POSTS_LIKE_BUFFER_CACHE_KEY_PREFIX.replace(
                "{postId}",
                postId.toString()
        );

        Set<String> allBuffers = setOps.members(key);

        if (allBuffers != null) {

            for (String buffer : allBuffers) {
                try {
                    JsonNode node = objectMapper.readTree(buffer);
                    if (node.get("userId").asLong() == loggedInUserId) {
                        return true;
                    }
                } catch (JsonProcessingException e) {
                    // 무시하고 다음 항목 계속 진행
                }
            }
        }

        return null;
    }
}
