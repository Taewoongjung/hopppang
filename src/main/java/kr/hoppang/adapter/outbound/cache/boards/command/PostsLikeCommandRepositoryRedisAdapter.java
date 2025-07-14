package kr.hoppang.adapter.outbound.cache.boards.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

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
        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

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

            Long added = setOps.add(likeInfoKey, redisValue);

            if (added != null && added == 1L) {
                valueOps.increment(countKey);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis value 직렬화 실패", e);
        }
    }

    @Override
    public void createAll(List<PostsLike> postsLikes) {
        // yet developed
    }

    @Override
    public void delete(PostsLike postsLike) {
        // yet developed
    }
}
