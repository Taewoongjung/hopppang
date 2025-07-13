package kr.hoppang.application.readmodel.boards.queries;

import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

@Builder
public record FindPostsRepliesCountsOfLikesByIdsQuery(
        List<Long> replyIdList
) implements IQuery { }
