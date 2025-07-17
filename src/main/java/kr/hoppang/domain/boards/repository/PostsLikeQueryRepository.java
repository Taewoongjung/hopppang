package kr.hoppang.domain.boards.repository;

import java.util.List;
import java.util.Map;

public interface PostsLikeQueryRepository {

    BoardsRepositoryStrategy strategy();

    Map<Long, Long> findCountOfLikesByPostIds(List<Long> postIds);
}
