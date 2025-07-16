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
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository("PostsViewCommandRepositoryRedis")
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
                            "userId", (
                                    postsView.getUserId() == null ?
                                            "null"
                                            : postsView.getUserId().toString()
                            ),
                            "clickedAt", postsView.getClickedAt()
                    )
            );

            setOps.add(viewBufferKey, redisValue);

            String script = """
                        local key = KEYS[1]
                        local expireTime = tonumber(ARGV[1])
                        local incrementValue = tonumber(ARGV[2])
                    
                        -- 현재 값 가져오기
                        local currentValue = redis.call('GET', key)
                        local cleanValue
                    
                        if currentValue == false then
                            cleanValue = 0
                        else
                            -- NULL 바이트 제거 및 숫자 변환. (숫자 욍 쓰레기값 들어가는거 방지하기 위함)
                            cleanValue = string.gsub(currentValue, '%z', '')  -- NULL 바이트 제거
                            cleanValue = string.gsub(cleanValue, '[^0-9%-]', '')  -- 숫자가 아닌 문자 제거
                            cleanValue = tonumber(cleanValue) or 0
                        end
                    
                        local newValue = cleanValue + incrementValue
                        redis.call('SET', key, tostring(newValue))
                        redis.call('EXPIRE', key, expireTime)
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
    public void createAll(final List<PostsView> postsViewList) {
        // 카운트 수만 다시 캐싱 하는 로직
        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        postsViewList.forEach(postsView -> {
            String viewCountKey = POSTS_VIEW_COUNT_CACHE_KEY_PREFIX.replace(
                    "{postId}",
                    postsView.getPostId().toString()
            );

            valueOps.set(
                    viewCountKey,
                    (
                            postsView.getOriginCount() == null ?
                                    "0" : postsView.getOriginCount().toString()
                    ),
                    Duration.ofMinutes(10).getSeconds()
            );
        });
    }
}
