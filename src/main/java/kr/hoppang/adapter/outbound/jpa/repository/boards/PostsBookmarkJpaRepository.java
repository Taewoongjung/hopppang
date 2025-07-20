package kr.hoppang.adapter.outbound.jpa.repository.boards;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.bookmark.PostsBookmarkEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostsIsBookmarkedDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostsBookmarkJpaRepository extends JpaRepository<PostsBookmarkEntity, Long> {

    boolean existsById_PostIdAndId_UserId(
            @Param("postId") Long postId,
            @Param("userId") Long userId
    );

    @Query(
            """
                SELECT new kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostsIsBookmarkedDto(
                    p.id.postId,
                    COUNT(*)
                )
                FROM PostsBookmarkEntity p
                WHERE p.id.postId IN :postIds AND p.id.userId = :userId
                GROUP BY p.id.postId
            """
    )
    List<PostsIsBookmarkedDto> isBookmarkedGroupByPostIdAndUserId(
            @Param("postIds") List<Long> postIds,
            @Param("userId") Long userId
    );
}
