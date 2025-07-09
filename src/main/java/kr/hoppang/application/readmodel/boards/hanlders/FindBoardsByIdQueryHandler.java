package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindBoardsByIdQuery;
import kr.hoppang.domain.boards.Boards;
import kr.hoppang.domain.boards.repository.BoardsQueryRepository;
import kr.hoppang.domain.boards.repository.BoardsQueryStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindBoardsByIdQueryHandler implements IQueryHandler<FindBoardsByIdQuery, Boards> {

    private final List<BoardsQueryRepository> boardsQueryRepositoryList;

    private EnumMap<BoardsQueryStrategy, BoardsQueryRepository> boardsQueryRepositoryMap;

    @PostConstruct
    void init() {
        boardsQueryRepositoryMap = boardsQueryRepositoryList.stream()
                .collect(Collectors.toMap(
                        BoardsQueryRepository::getBoardsQueryStrategy,
                        (boardsQueryRepository) -> boardsQueryRepository,
                        (existBoardsQueryRepository, newBoardsQueryRepository) -> existBoardsQueryRepository,
                        () -> new EnumMap<>(BoardsQueryStrategy.class)
                ));
    }


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    public Boards handle(final FindBoardsByIdQuery query) {

        Boards board = boardsQueryRepositoryMap.get(BoardsQueryStrategy.CACHE)
                .getBoardsById(query.boardId());

        if (board == null) {
            board = boardsQueryRepositoryMap.get(BoardsQueryStrategy.RDB)
                    .getBoardsById(query.boardId());
        }

        return board;
    }
}
