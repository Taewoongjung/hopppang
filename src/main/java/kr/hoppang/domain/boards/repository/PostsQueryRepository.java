package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.dto.ConditionOfFindPosts;

public interface PostsQueryRepository {

    List<Posts> findAllPostsByCondition(final ConditionOfFindPosts condition);

    long countAllPostsByCondition(final ConditionOfFindPosts condition);

    Posts findPostsByPostId(final long postId);
}
