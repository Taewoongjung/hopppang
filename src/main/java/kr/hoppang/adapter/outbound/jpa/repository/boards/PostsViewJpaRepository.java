package kr.hoppang.adapter.outbound.jpa.repository.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.view.PostsViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsViewJpaRepository extends JpaRepository<PostsViewEntity, Long> {

}
