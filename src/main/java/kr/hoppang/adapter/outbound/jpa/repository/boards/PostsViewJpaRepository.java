package kr.hoppang.adapter.outbound.jpa.repository.boards;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.view.PostsViewEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostViewCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostsViewJpaRepository extends JpaRepository<PostsViewEntity, Long> {

    @Query(
            """
                SELECT new kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostViewCountDto(
                    p.id.postId,
                    COUNT(p)
                )
                FROM PostsViewEntity p
                WHERE p.id.postId IN :postIds
                GROUP BY p.id.postId
            """
    )
    List<PostViewCountDto> countViewsGroupedByPostIds(@Param("postIds") List<Long> postIds);
}
