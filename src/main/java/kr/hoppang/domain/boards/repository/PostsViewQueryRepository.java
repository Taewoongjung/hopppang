package kr.hoppang.domain.boards.repository;

import java.util.List;
import java.util.Map;

public interface PostsViewQueryRepository {

    BoardsRepositoryStrategy strategy();

    Map<Long, Long> findCountOfViewsByPostIds(List<Long> postIds);
}
