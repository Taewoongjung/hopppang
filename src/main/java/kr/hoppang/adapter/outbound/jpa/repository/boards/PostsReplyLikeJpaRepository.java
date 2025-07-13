package kr.hoppang.adapter.outbound.jpa.repository.boards;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsReplyLikeEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.ReplyLikeCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostsReplyLikeJpaRepository extends JpaRepository<PostsReplyLikeEntity, Long> {

    @Query(
        """
            SELECT new kr.hoppang.adapter.outbound.jpa.repository.boards.dto.ReplyLikeCountDto(
                p.id.postReplyId,
                COUNT(p)
            )
            FROM PostsReplyLikeEntity p
            WHERE p.id.postReplyId IN :replyIds
            GROUP BY p.id.postReplyId
        """
    )
    List<ReplyLikeCountDto> countLikesGroupedByReplyId(@Param("replyIds") List<Long> replyIds);
}
