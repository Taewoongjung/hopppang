package kr.hoppang.adapter.inbound.chassis.admin.webdto;

import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;

public record GetEstimatedByStateStatisticsWebDtoV1() {

    @Builder(access = AccessLevel.PRIVATE)
    public record Res(
            List<StatisticsElement> estimationByStateStatisticsData
    ) {

        @Builder(access = AccessLevel.PRIVATE)
        private record StatisticsElement(
                String state,
                String percentile,
                int count
        ) { }


        public static GetEstimatedByStateStatisticsWebDtoV1.Res of(
                Map<String, Integer> EstimatedCountByState,
                long totalEstimationCount
        ) {

            return Res.builder()
                    .estimationByStateStatisticsData(
                            EstimatedCountByState.keySet().stream()
                                    .map(state ->
                                            StatisticsElement.builder()
                                                    .state(state)
                                                    .percentile(
                                                            getPercentile(
                                                                    EstimatedCountByState.get(
                                                                            state),
                                                                    totalEstimationCount
                                                            )
                                                    )
                                                    .count(EstimatedCountByState.get(state))
                                                    .build()
                                    )
                                    .toList()
                    )
                    .build();
        }

        private static String getPercentile(final int count, final long totalCount) {
            if (totalCount == 0) {
                return "0.0%";
            }

            double percentage = ((double) count / totalCount) * 100;
            return String.format("%.1f%%", percentage);
        }
    }
}
