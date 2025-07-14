package kr.hoppang.adapter.outbound.cache.boards.readmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository("PostsReplyLikeQueryRepositoryRedis")
@RequiredArgsConstructor
public class PostsReplyLikeQueryRepositoryRedisAdapter implements PostsReplyLikeQueryRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String POSTS_REPLY_LIKE_COUNT_CACHE_KEY_PREFIX = "reply:likes:count:{replyId}";


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.CACHE;
    }

    @Override
    public Map<Long, Long> findCountOfLikesByPostId(final List<Long> replyIds) {
        Map<Long, Long> result = new HashMap<>();

        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        for (Long replyId : replyIds) {
            String key = POSTS_REPLY_LIKE_COUNT_CACHE_KEY_PREFIX.replace(
                    "{replyId}",
                    replyId.toString()
            );

            String value = valueOps.get(key);

            if (value != null) {
                try {
                    Long count = Long.parseLong(value);

                    result.put(replyId, count);

                } catch (NumberFormatException ignored) {
                    // 잘못된 값이 들어가 있을 경우 예외 방지
                }
            }
        }

        return result;
    }
}