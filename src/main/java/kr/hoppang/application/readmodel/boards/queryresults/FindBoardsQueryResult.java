package kr.hoppang.application.readmodel.boards.queryresults;

import lombok.Builder;

@Builder
public record FindBoardsQueryResult(
        long id,
        String name
) { }
