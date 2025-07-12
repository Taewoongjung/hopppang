package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.PostsReplyLike;

public interface PostsReplyLikeCommandRepository {

    void create(PostsReplyLike postsReplyLike);

    void delete(PostsReplyLike postsReplyLike);
}
