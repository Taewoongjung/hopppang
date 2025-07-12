package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.PostsLike;

public interface PostsLikeCommandRepository {

    void create(PostsLike postsLike);

    void delete(PostsLike postsLike);
}
