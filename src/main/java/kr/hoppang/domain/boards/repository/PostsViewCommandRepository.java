package kr.hoppang.domain.boards.repository;

import kr.hoppang.domain.boards.PostsView;

public interface PostsViewCommandRepository {

    void create(PostsView postsView);
}
