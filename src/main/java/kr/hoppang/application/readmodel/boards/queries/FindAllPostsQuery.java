package kr.hoppang.application.readmodel.boards.queries;

import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

@Builder
public record FindAllPostsQuery(
        List<Long> boardIds,
        Long userId,
        String searchWord,
        Boolean bookmarkOnly,
        Boolean repliesOnly,
        int limit,
        long offset
) implements IQuery { }
