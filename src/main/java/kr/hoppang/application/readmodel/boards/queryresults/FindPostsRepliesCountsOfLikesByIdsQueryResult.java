package kr.hoppang.application.readmodel.boards.queryresults;

import java.util.Map;
import lombok.Builder;

@Builder
public record FindPostsRepliesCountsOfLikesByIdsQueryResult(
        Map<Long, Long> countDatas
) { }
