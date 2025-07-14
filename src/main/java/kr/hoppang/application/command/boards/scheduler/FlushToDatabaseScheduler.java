package kr.hoppang.application.command.boards.scheduler;

import static kr.hoppang.adapter.outbound.cache.boards.command.PostsLikeCommandRepositoryRedisAdapter.POSTS_LIKE_BUFFER_CACHE_KEY_PREFIX;
import static kr.hoppang.adapter.outbound.cache.boards.command.PostsReplyLikeCommandRepositoryRedisAdapter.POSTS_REPLY_LIKE_BUFFER_CACHE_KEY_PREFIX;
import static kr.hoppang.adapter.outbound.cache.boards.command.PostsViewCommandRepositoryRedisAdapter.POSTS_VIEW_BUFFER_CACHE_KEY_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.PostsView;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import kr.hoppang.domain.boards.repository.PostsViewCommandRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlushToDatabaseScheduler {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final PostsLikeCommandRepository postsLikeCommandRepository;
    private final PostsViewCommandRepository postsViewCommandRepository;
    private final PostsReplyLikeCommandRepository postsReplyLikeCommandRepository;


    @Scheduled(cron = "0 */10 * * * *")
    public void flushReplyLikesToDb() {
        Set<String> replyLikeBufferKeys = redisTemplate.keys(
                POSTS_REPLY_LIKE_BUFFER_CACHE_KEY_PREFIX.replace("{replyId}", "*")
        );

        if (replyLikeBufferKeys.isEmpty()) return;

        List<RedisReplyLikeEntry> dbFlushTargetList = new ArrayList<>();

        List<Long> flushedReplyIds = new ArrayList<>();

        for (String replyRedisKey : replyLikeBufferKeys) {

            Long replyId = extractReplyId(replyRedisKey);

            flushedReplyIds.add(replyId);

            Set<String> valueSet = redisTemplate.opsForSet().members(replyRedisKey);

            if (valueSet == null || valueSet.isEmpty()) continue;

            for (String json : valueSet) {
                try {
                    RedisReplyLikeEntry entry = objectMapper.readValue(json, RedisReplyLikeEntry.class);
                    entry.setPostReplyId(replyId);
                    dbFlushTargetList.add(entry);
                } catch (Exception e) {
                    log.error("댓글 좋아요 반영 실패: replyId={}, raw={}, error={}", replyId, json, e.getMessage(), e);
                }
            }
        }

        if (!dbFlushTargetList.isEmpty()) {
            try {
                postsReplyLikeCommandRepository.createAll(
                        dbFlushTargetList.stream()
                                .map(e ->
                                        PostsReplyLike.builder()
                                                .postReplyId(e.postReplyId)
                                                .userId(e.userId)
                                                .clickedAt(e.clickedAt)
                                                .build()
                                ).toList()
                );
            } catch (Exception ignored) {
                // 유니크 키 중복 등은 무시
            }
        }

        // Redis 캐시 삭제 (무효화)
        replyLikeBufferKeys.forEach(redisTemplate::delete);
    }

    private Long extractReplyId(final String key) {
        String[] parts = key.split(":");
        return Long.valueOf(parts[parts.length - 1]);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RedisReplyLikeEntry {
        @Setter
        private Long postReplyId;
        private Long userId;
        private LocalDateTime clickedAt;
    }


    @Scheduled(cron = "0 */10 * * * *")
    public void flushPostsLikesToDb() {
        Set<String> keys = redisTemplate.keys(
                POSTS_LIKE_BUFFER_CACHE_KEY_PREFIX.replace("{replyId}", "*")
        );

        if (keys.isEmpty()) return;

        List<RedisPostLikeEntry> dbFlushTargetList = new ArrayList<>();

        for (String postRedisKey : keys) {
            Long postId = extractPostId(postRedisKey);
            Set<String> valueSet = redisTemplate.opsForSet().members(postRedisKey);

            if (valueSet == null || valueSet.isEmpty()) continue;

            for (String json : valueSet) {
                try {
                    RedisPostLikeEntry entry = objectMapper.readValue(json, RedisPostLikeEntry.class);
                    entry.setPostId(postId);
                    dbFlushTargetList.add(entry);
                } catch (Exception e) {
                    log.error("게시글 좋아요 반영 실패: postId={}, raw={}, error={}", postId, json, e.getMessage(), e);
                }
            }
        }

        if (!dbFlushTargetList.isEmpty()) {
            try {
                postsLikeCommandRepository.createAll(
                        dbFlushTargetList.stream()
                                .map(e ->
                                        PostsLike.builder()
                                                .postId(e.postId)
                                                .userId(e.userId)
                                                .clickedAt(e.clickedAt)
                                                .build()
                                ).toList()
                );
            } catch (Exception ignored) {
                // 유니크 키 중복 등은 무시
            }
        }

        // Redis 캐시 삭제 (무효화)
        keys.forEach(redisTemplate::delete);
    }

    private Long extractPostId(final String key) {
        String[] parts = key.split(":");
        return Long.valueOf(parts[parts.length - 1]);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RedisPostLikeEntry {
        @Setter
        private Long postId;
        private Long userId;
        private LocalDateTime clickedAt;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void flushPostsViewsToDb() {
        Set<String> keys = redisTemplate.keys(
                POSTS_VIEW_BUFFER_CACHE_KEY_PREFIX.replace("{postId}", "*")
        );

        if (keys.isEmpty()) return;

        List<RedisPostViewEntry> dbFlushTargetList = new ArrayList<>();

        for (String postRedisKey : keys) {
            Long postId = extractPostId(postRedisKey);
            Set<String> valueSet = redisTemplate.opsForSet().members(postRedisKey);

            if (valueSet == null || valueSet.isEmpty()) continue;

            for (String json : valueSet) {
                try {
                    RedisPostViewEntry entry = objectMapper.readValue(json, RedisPostViewEntry.class);
                    entry.setPostId(postId);
                    dbFlushTargetList.add(entry);
                } catch (Exception e) {
                    log.error("게시글 조회수 반영 실패: postId={}, raw={}, error={}", postId, json, e.getMessage(), e);
                }
            }
        }

        if (!dbFlushTargetList.isEmpty()) {
            try {
                postsViewCommandRepository.createAll(
                        dbFlushTargetList.stream()
                                .map(e ->
                                        PostsView.builder()
                                                .postId(e.postId)
                                                .userId(e.userId)
                                                .clickedAt(e.clickedAt)
                                                .build()
                                ).toList()
                );
            } catch (Exception ignored) {
                // 유니크 키 중복 등은 무시
            }
        }

        // Redis 캐시 삭제 (무효화)
        keys.forEach(redisTemplate::delete);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RedisPostViewEntry {
        @Setter
        private Long postId;
        private Long userId;
        private LocalDateTime clickedAt;
    }
}
