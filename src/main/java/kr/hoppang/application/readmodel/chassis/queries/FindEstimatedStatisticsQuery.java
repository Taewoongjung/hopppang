package kr.hoppang.application.readmodel.chassis.queries;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_REQUEST;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.statistics.SearchPeriodType;
import lombok.AccessLevel;
import lombok.Builder;

public record FindEstimatedStatisticsQuery() {

    @Builder
    public record Req(
            SearchPeriodType searchPeriodType,
            Integer searchPeriodValue,
            LocalDateTime now
    ) implements IQuery {

        public LocalDateTime getStartTimeForSearch() {
            check((searchPeriodValue == null || searchPeriodValue <= 0), INVALID_REQUEST);

            if (searchPeriodType.equals(SearchPeriodType.DAILY)) {
                return now.minusDays(searchPeriodValue).toLocalDate().atStartOfDay();
            }

            if (searchPeriodType.equals(SearchPeriodType.WEEKLY)) {
                return now.minusWeeks(searchPeriodValue).toLocalDate().atStartOfDay();
            }

            return now.minusMonths(searchPeriodValue).toLocalDate().atStartOfDay();
        }

        public LocalDateTime getEndTimeForSearch() {
            LocalDate today = LocalDate.now();

            return LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(),
                    23, 59, 59);
        }

        // 범위를 LocalDateTime 객체로 표현 (범위를 필터링 하기 쉽게 하기 위함)
        public List<List<LocalDateTime>> getStatisticsRangeList() {

            List<List<LocalDateTime>> statisticsRangeList = new ArrayList<>();

            if (searchPeriodType.equals(SearchPeriodType.DAILY)) {
                for (int i = 0; i < searchPeriodValue; i++) {
                    statisticsRangeList.add(
                            List.of(
                                    now.minusDays(i)
                            )
                    );
                }
            }

            if (searchPeriodType.equals(SearchPeriodType.WEEKLY)) {
                List<LocalDateTime> bundle = new ArrayList<>();
                for (int i = 0; i < searchPeriodValue; i++) {
                    for (int j = i * 7; j < (i + 1) * 7; j++) {
                        bundle.add(now.minusDays(j));
                    }
                }

                statisticsRangeList = getStatisticsWeeklyRangeList(bundle);
            }

            if (searchPeriodType.equals(SearchPeriodType.MONTH)) {
                for (int i = 0; i < searchPeriodValue; i++) {
                    statisticsRangeList.add(List.of(now.minusMonths(i)));
                }
            }

            return statisticsRangeList;
        }

        private List<List<LocalDateTime>> getStatisticsWeeklyRangeList(final List<LocalDateTime> weeklyLabelList) {

            if (weeklyLabelList.isEmpty()) {
                return new ArrayList<>();
            }

            if (weeklyLabelList.size() < 7) {
                return List.of(
                        List.of(
                                weeklyLabelList.get(0),
                                weeklyLabelList.get(weeklyLabelList.size() - 1)
                        )
                );
            }

            List<List<LocalDateTime>> labelRangeList = new ArrayList<>();
            for (int i = 0; i < weeklyLabelList.size(); i += 7) {
                labelRangeList.add(
                        List.of(
                                weeklyLabelList.get(i),
                                weeklyLabelList.get(i + 6)
                        )
                );
            }

            return labelRangeList;
        }

    }

    @Builder(access = AccessLevel.PRIVATE)
    public record Res(
            List<Res.StatisticsElement> registeredChassisEstimationsStatisticsElementList
    ) {

        @Builder
        public record StatisticsElement(
                int sequence,
                String label,
                int count
        ) { }

        public static Res of(
                final List<Res.StatisticsElement> registeredChassisEstimationsStatisticsElementList
        ) {
            return Res.builder()
                    .registeredChassisEstimationsStatisticsElementList(
                            registeredChassisEstimationsStatisticsElementList)
                    .build();
        }
    }
}
