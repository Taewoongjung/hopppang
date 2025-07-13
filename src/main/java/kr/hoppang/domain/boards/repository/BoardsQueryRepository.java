package kr.hoppang.domain.boards.repository;

import java.util.List;
import kr.hoppang.domain.boards.Boards;

public interface BoardsQueryRepository {

    BoardsRepositoryStrategy getBoardsQueryStrategy();

    List<Boards> getAllBoards();

    Boards getBoardsById(long boardsId);
}
