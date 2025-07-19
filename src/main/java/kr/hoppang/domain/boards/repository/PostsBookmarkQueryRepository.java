package kr.hoppang.domain.boards.repository;

import java.util.List;
import java.util.Map;

public interface PostsBookmarkQueryRepository {

    Boolean isMarkedByPostsIdAndUserId(Long postsId, Long userId);

    Map<Long, Boolean> isBookmarkedGroupByPostsIdAndUserId(List<Long> postsId, Long userId);
}
