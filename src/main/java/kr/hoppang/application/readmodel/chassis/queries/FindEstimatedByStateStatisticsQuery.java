package kr.hoppang.application.readmodel.chassis.queries;

import java.util.Map;
import lombok.Builder;

public record FindEstimatedByStateStatisticsQuery() {

    @Builder
    public record Res(
            Map<String, Integer> countOfEstimatedByState,
            long totalEstimationCount
    ) { }
}
