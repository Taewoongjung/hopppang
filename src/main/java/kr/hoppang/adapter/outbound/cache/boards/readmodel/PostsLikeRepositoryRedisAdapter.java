package kr.hoppang.adapter.outbound.cache.boards.readmodel;

import static kr.hoppang.adapter.outbound.cache.boards.command.PostsLikeCommandRepositoryRedisAdapter.POSTS_LIKE_COUNT_CACHE_KEY_PREFIX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostsLikeRepositoryRedisAdapter implements PostsLikeQueryRepository {

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
    public boolean isLikedByPostId(Long postId, Long loggedInUserId) {
        // yet developed
        return false;
    }
}
