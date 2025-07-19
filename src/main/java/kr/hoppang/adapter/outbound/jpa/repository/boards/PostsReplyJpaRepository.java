package kr.hoppang.adapter.outbound.jpa.repository.boards;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.PostsReplyEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostReplyCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostsReplyJpaRepository extends JpaRepository<PostsReplyEntity, Long> {

    @Query(
            """
                SELECT new kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostReplyCountDto(
                    p.postId,
                    COUNT(*)
                )
                FROM PostsReplyEntity p
                WHERE p.postId IN :postIds
                GROUP BY p.postId
            """
    )
    List<PostReplyCountDto> countRepliesGroupedByPostId(@Param("postIds") List<Long> postIds);

    @Modifying
    @Query("UPDATE PostsReplyEntity p SET p.isDeleted = 'T' WHERE p.id = :replyId")
    void reviseAsDeletedById(@Param("replyId") final long replyId);
}
