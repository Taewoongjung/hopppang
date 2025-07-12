package kr.hoppang.adapter.outbound.jpa.repository.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsLikeJpaRepository extends JpaRepository<PostsLikeEntity, Long> {

}
