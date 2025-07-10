package kr.hoppang.adapter.outbound.jpa.repository.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.PostsReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsReplyJpaRepository extends JpaRepository<PostsReplyEntity, Long> {

}
