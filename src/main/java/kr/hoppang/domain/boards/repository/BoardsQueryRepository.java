package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.Boards;

public interface BoardsQueryRepository {

    BoardsQueryStrategy getBoardsQueryStrategy();

    List<Boards> getAllBoards();

    Boards getBoardsById(long boardsId);
}
