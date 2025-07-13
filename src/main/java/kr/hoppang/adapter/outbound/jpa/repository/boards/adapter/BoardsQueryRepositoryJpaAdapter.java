package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import java.util.List;
import java.util.Optional;
import kr.hoppang.adapter.outbound.jpa.entity.board.BoardsEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.BoardsJpaRepository;
import kr.hoppang.domain.boards.Boards;
import kr.hoppang.domain.boards.repository.BoardsQueryRepository;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardsQueryRepositoryJpaAdapter implements BoardsQueryRepository {

    private final BoardsJpaRepository boardsJpaRepository;


    @Override
    public BoardsRepositoryStrategy getBoardsQueryStrategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public List<Boards> getAllBoards() {

        List<BoardsEntity> boardsEntityList = boardsJpaRepository.findAll();

        return boardsEntityList.stream()
                .map(BoardsEntity::toPojo)
                .toList();
    }

    @Override
    public Boards getBoardsById(final long boardsId) {
        Optional<BoardsEntity> boards = boardsJpaRepository.findById(boardsId);

        return boards.map(BoardsEntity::toPojo)
                .orElse(null);
    }
}
