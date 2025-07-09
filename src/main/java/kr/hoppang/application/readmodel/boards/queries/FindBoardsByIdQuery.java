package kr.hoppang.application.readmodel.boards.queries;

import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

@Builder
public record FindBoardsByIdQuery(
        long boardId
) implements IQuery { }
