package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.PostsReply;

public interface PostsReplyCommandRepository {

    Long create(PostsReply postsReply);
}
