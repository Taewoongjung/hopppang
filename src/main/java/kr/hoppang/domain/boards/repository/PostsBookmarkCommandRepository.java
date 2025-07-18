package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.PostsBookmark;

public interface PostsBookmarkCommandRepository {

    void create(PostsBookmark postsBookmark);

    void delete(PostsBookmark postsBookmark);
}
