package kr.hoppang.adapter.outbound.cache.boards.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import kr.hoppang.domain.boards.PostsView;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsViewCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

@Repository("PostsViewCommandRepositoryRedis")
@RequiredArgsConstructor
public class PostsViewCommandRepositoryRedisAdapter implements PostsViewCommandRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public static final String POSTS_VIEW_COUNT_CACHE_KEY_PREFIX = "post:views:count:{postId}";
    public static final String POSTS_VIEW_BUFFER_CACHE_KEY_PREFIX = "post:views:buffer:{postId}";


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.CACHE;
    }

    @Override
    public void create(final PostsView postsView) {
        final SetOperations<String, String> setOps = redisTemplate.opsForSet();

        String viewBufferKey = POSTS_VIEW_BUFFER_CACHE_KEY_PREFIX.replace(
                "{postId}",
                postsView.getPostId().toString()
        );

        String viewCountKey = POSTS_VIEW_COUNT_CACHE_KEY_PREFIX.replace(
                "{postId}",
                postsView.getPostId().toString()
        );

        try {
            String redisValue = objectMapper.writeValueAsString(
                    Map.of(
                            "userId", postsView.getUserId(),
                            "clickedAt", postsView.getClickedAt()
                    )
            );

            setOps.add(viewBufferKey, redisValue);

            String script = """
                                local newValue = redis.call('INCRBY', KEYS[1], tonumber(ARGV[2]))
                                redis.call('EXPIRE', KEYS[1], tonumber(ARGV[1]))
                                return newValue
                            """;

            redisTemplate.execute(
                    RedisScript.of(script, Long.class),
                    List.of(viewCountKey),
                    String.valueOf(Duration.ofMinutes(10).getSeconds()),  // ARGV[1]: expire time
                    String.valueOf(
                            postsView.getOriginCount() != null ?
                                    postsView.getOriginCount() : 1
                    )  // ARGV[2]: originCount
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis value 직렬화 실패", e);
        }
    }

    @Override
    public void createAll(List<PostsView> postsViewList) {
        // yet developed
    }
}
