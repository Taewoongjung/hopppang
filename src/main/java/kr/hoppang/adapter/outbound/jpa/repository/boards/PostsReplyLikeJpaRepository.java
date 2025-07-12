package kr.hoppang.adapter.outbound.jpa.repository.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsReplyLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsReplyLikeJpaRepository extends JpaRepository<PostsReplyLikeEntity, Long> {

}
