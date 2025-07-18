package kr.hoppang.domain.boards.repository;

public interface PostsBookmarkQueryRepository {

    Boolean isMarkedByPostsIdAndUserId(Long postsId, Long userId);
}
