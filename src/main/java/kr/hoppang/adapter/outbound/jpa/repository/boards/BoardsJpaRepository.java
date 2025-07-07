package kr.hoppang.adapter.outbound.jpa.repository.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.BoardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsJpaRepository extends JpaRepository<BoardsEntity, Long> {

}
