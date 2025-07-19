package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.PostsReply;

public interface PostsReplyCommandRepository {

    Long create(PostsReply postsReply);

    boolean revise(PostsReply revisingPostsReply);

    void delete(Long postsReplyId);
}
