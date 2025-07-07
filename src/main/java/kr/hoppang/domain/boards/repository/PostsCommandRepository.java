package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.Posts;

public interface PostsCommandRepository {

    void create(final Posts newPost);
}
