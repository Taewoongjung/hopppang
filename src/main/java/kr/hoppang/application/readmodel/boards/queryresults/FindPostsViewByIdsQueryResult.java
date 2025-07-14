package kr.hoppang.application.readmodel.boards.queryresults;

import java.util.Map;
import lombok.Builder;

@Builder
public record FindPostsViewByIdsQueryResult(
        Map<Long, Long> viewCountDatas
) { }
