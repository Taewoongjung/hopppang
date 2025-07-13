package kr.hoppang.adapter.outbound.cache.boards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

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
                valueOps.increment(countKey);
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
        // reply:likes:buffer:{replyId} 무효화
    }
}
