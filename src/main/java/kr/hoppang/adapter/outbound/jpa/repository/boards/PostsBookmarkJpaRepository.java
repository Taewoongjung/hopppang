package kr.hoppang.adapter.outbound.jpa.repository.boards;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hoppang.adapter.outbound.jpa.entity.board.bookmark.PostsBookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsBookmarkJpaRepository extends JpaRepository<PostsBookmarkEntity, Long> {

    boolean existsById_PostIdAndId_UserId(
            @Param("postId") Long postId,
            @Param("userId") Long userId
    );
}
