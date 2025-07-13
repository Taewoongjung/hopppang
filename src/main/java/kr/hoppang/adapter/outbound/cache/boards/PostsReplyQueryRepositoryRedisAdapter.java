package kr.hoppang.adapter.outbound.cache.boards;

import static kr.hoppang.adapter.outbound.cache.boards.PostsReplyLikeCommandRepositoryRedisAdapter.POSTS_REPLY_LIKE_BUFFER_CACHE_KEY_PREFIX;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostsReplyQueryRepositoryRedisAdapter implements PostsReplyQueryRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.CACHE;
    }

    @Override
    public List<PostsReply> findPostsReplyByPostId(final long postId) {
        // yet developed
        return List.of();
    }

    @Override
    public List<Long> findAllLikedReplyIdsByUserId(final List<Long> replyIds, final long userId) {
        final SetOperations<String, String> setOps = redisTemplate.opsForSet();

        List<Long> likedReplyListByTargetUser = new ArrayList();

        for (Long replyId : replyIds) {

            String key = POSTS_REPLY_LIKE_BUFFER_CACHE_KEY_PREFIX.replace(
                    "{replyId}",
                    replyId.toString()
            );

            Set<String> likeInfoList = setOps.members(key);

            if (likeInfoList != null) {
                for (String member : likeInfoList) {
                    try {
                        JsonNode node = objectMapper.readTree(member);
                        if (node.get("userId").asLong() == userId) {
                            likedReplyListByTargetUser.add(replyId);
                        }
                    } catch (JsonProcessingException ignored) {
                    }
                }
            }
        }

        return likedReplyListByTargetUser;
    }
}
