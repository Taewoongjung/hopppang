package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.Posts;

public interface PostsCommandRepository {

    Long create(Posts newPost);

    boolean revise(Posts revisedPost);

    void delete(Long postId);
}
