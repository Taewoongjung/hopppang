package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.Posts;

public interface PostsCommandRepository {

    Long create(final Posts newPost);
}
