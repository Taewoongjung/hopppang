package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.PostsReply;

public interface PostsReplyQueryRepository {

    BoardsRepositoryStrategy strategy();

    List<PostsReply> findPostsReplyByPostId(long postId);

    List<Long> findAllLikedReplyIdsByUserId(List<Long> replyIds, long userId);
}
