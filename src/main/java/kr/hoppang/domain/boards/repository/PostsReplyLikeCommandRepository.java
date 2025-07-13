package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.PostsReplyLike;

public interface PostsReplyLikeCommandRepository {

    BoardsRepositoryStrategy strategy();

    void create(PostsReplyLike postsReplyLike);

    void createAll(List<PostsReplyLike> postsReplyLikeList);

    void delete(PostsReplyLike postsReplyLike);
}
