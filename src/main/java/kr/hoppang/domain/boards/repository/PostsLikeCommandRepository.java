package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.PostsLike;

public interface PostsLikeCommandRepository {

    BoardsRepositoryStrategy strategy();

    void create(PostsLike postsLike);

    void createAll(List<PostsLike> postsLikes);

    void delete(PostsLike postsLike);
}
