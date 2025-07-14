package kr.hoppang.adapter.outbound.cache.boards.readmodel;

import static kr.hoppang.adapter.outbound.cache.boards.command.PostsViewCommandRepositoryRedisAdapter.POSTS_VIEW_COUNT_CACHE_KEY_PREFIX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsViewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostsViewQueryRepositoryRedisAdapter implements PostsViewQueryRepository {

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.CACHE;
    }

    @Override
    public Map<Long, Long> findCountOfViewsByPostIds(final List<Long> postIds) {
        Map<Long, Long> result = new HashMap<>();

        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        for (Long postId : postIds) {
            String key = POSTS_VIEW_COUNT_CACHE_KEY_PREFIX.replace(
                    "{postId}",
                    postId.toString()
            );

            String value = valueOps.get(key);

            if (value == null || value.isBlank()) {
                return null;
            }

            result.put(postId, Long.parseLong(value));
        }

        return result;
    }
}
