package kr.hoppang.domain.boards.repository;

import java.util.List;
import java.util.Map;

public interface PostsReplyLikeQueryRepository {

    BoardsRepositoryStrategy strategy();

    Map<Long, Long> findCountOfLikesByPostId(List<Long> replyIds);
}
