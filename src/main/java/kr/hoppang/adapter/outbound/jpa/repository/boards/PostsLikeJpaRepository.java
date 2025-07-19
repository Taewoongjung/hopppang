package kr.hoppang.adapter.outbound.jpa.repository.boards;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsLikeEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostLikeCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostsLikeJpaRepository extends JpaRepository<PostsLikeEntity, Long> {

    @Query(
            """
                SELECT new kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostLikeCountDto(
                    p.id.postId,
                    COUNT(*)
                )
                FROM PostsLikeEntity p
                WHERE p.id.postId IN :postIds
                GROUP BY p.id.postId
            """
    )
    List<PostLikeCountDto> countLikesGroupedByPostIds(@Param("postIds") List<Long> postIds);

    boolean existsById_PostIdAndId_UserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
