package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.PostsReply;

public interface PostsReplyQueryRepository {

    List<PostsReply> findPostsReplyByPostId(Long postId);
}
