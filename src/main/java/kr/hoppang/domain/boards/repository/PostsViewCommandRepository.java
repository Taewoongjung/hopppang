package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.PostsView;

public interface PostsViewCommandRepository {

    BoardsRepositoryStrategy strategy();

    void create(PostsView postsView);

    void createAll(List<PostsView> postsViewList);
}
