package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.boards.queryresults.FindBoardsQueryResult;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.domain.boards.Boards;
import kr.hoppang.domain.boards.repository.BoardsQueryRepository;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindBoardsQueryHandler implements IQueryHandler<EmptyQuery, List<FindBoardsQueryResult>> {

    private final List<BoardsQueryRepository> boardsQueryRepositoryList;

    private EnumMap<BoardsRepositoryStrategy, BoardsQueryRepository> boardsQueryRepositoryMap;

    @PostConstruct
    void init() {
        boardsQueryRepositoryMap = boardsQueryRepositoryList.stream()
                .collect(Collectors.toMap(
                        BoardsQueryRepository::getBoardsQueryStrategy,
                        (boardsQueryRepository) -> boardsQueryRepository,
                        (existBoardsQueryRepository, newBoardsQueryRepository) -> existBoardsQueryRepository,
                        () -> new EnumMap<>(BoardsRepositoryStrategy.class)
                ));
    }


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    public List<FindBoardsQueryResult> handle(EmptyQuery query) {

        List<Boards> boardsList = boardsQueryRepositoryMap.get(BoardsRepositoryStrategy.CACHE)
                .getAllBoards();

        if (boardsList == null || boardsList.isEmpty()) {
            boardsList = boardsQueryRepositoryMap.get(BoardsRepositoryStrategy.RDB)
                    .getAllBoards();
        }

        return boardsList.stream()
                .map(board -> FindBoardsQueryResult.builder()
                        .id(board.getId())
                        .rootId(board.getRootBoardId())
                        .name(board.getName())
                        .build()
                ).toList();
    }
}
