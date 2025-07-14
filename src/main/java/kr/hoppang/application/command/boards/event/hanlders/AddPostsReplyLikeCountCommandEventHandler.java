package kr.hoppang.application.command.boards.event.hanlders;

import static kr.hoppang.adapter.outbound.cache.boards.command.PostsReplyLikeCommandRepositoryRedisAdapter.POSTS_REPLY_LIKE_COUNT_CACHE_KEY_PREFIX;

import java.time.Duration;
import kr.hoppang.application.command.boards.event.events.AddPostsReplyLikeCountCommandEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddPostsReplyLikeCountCommandEventHandler {

    private final RedisTemplate<String, String> redisTemplate;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(final AddPostsReplyLikeCountCommandEvent event) {
        final ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        for (Long replyId : event.countOfLikesByReplyId().keySet()) {
            try {
                String key = POSTS_REPLY_LIKE_COUNT_CACHE_KEY_PREFIX.replace(
                        "{replyId}",
                        replyId.toString()
                );

                String countValue = String.valueOf(event.countOfLikesByReplyId().get(replyId));

                // TTL 30분 설정하면서 저장
                valueOps.set(key, countValue, Duration.ofMinutes(30));

            } catch (Exception e) {
                log.warn("Failed to cache like count for replyId {}: {}", replyId, e.getMessage());
            }
        }
    }
}
