package kr.hoppang.application.readmodel.user.queries;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_REQUEST;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.common.DayUtil.getKorDayOfWeek;
import static kr.hoppang.util.common.DayUtil.getKorMonth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.statistics.SearchPeriodType;
import lombok.AccessLevel;
import lombok.Builder;

public record FindStatisticsOfUserQuery() {

    @Builder
    public record Request(
            SearchPeriodType searchPeriodType,
            Integer searchPeriodValue,
            LocalDateTime now
    ) implements IQuery {

        // 실제 그래프의 x축 라벨링을 위함
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

        public List<String> getStatisticsLabelList() {

            List<String> statisticsLabelList = new ArrayList<>();

            if (searchPeriodType.equals(SearchPeriodType.DAILY)) {
                for (int i = 0; i < searchPeriodValue; i++) {
                    LocalDateTime targetDate = now.minusDays(i);
                    int dayOfWeekNum = targetDate.getDayOfWeek().getValue();
                    String dayOfWeek = getKorDayOfWeek(dayOfWeekNum);
                    statisticsLabelList.add(dayOfWeek);
                }
            }

            if (searchPeriodType.equals(SearchPeriodType.WEEKLY)) {
                for (int i = 0; i < searchPeriodValue; i++) {
                    for (int j = 1; j <= 7; j++) {

                        int dayOfMonth = (now.getDayOfMonth() - (i * 7)) - j + 1;
                        if (dayOfMonth <= 0) {
                            statisticsLabelList.add(
                                    String.valueOf(
                                            now.toLocalDate().minusMonths(i).lengthOfMonth()
                                    )
                            );
                        } else {
                            statisticsLabelList.add(String.valueOf(dayOfMonth));
                        }
                    }
                }

                statisticsLabelList = getStatisticsWeeklyLabelList(statisticsLabelList);
            }

            if (searchPeriodType.equals(SearchPeriodType.MONTH)) {
                for (int i = 0; i < searchPeriodValue; i++) {
                    LocalDateTime targetDate = now.minusMonths(i);
                    int month = targetDate.getMonthValue();
                    statisticsLabelList.add(getKorMonth(month));
                }
            }

            return statisticsLabelList;
        }

        private List<String> getStatisticsWeeklyLabelList(final List<String> weeklyLabelList) {

            if (weeklyLabelList.isEmpty()) {
                return new ArrayList<>();
            }

            String labelFormat = "(%s ~ %s)";
            if (weeklyLabelList.size() < 7) {
                return List.of(
                        String.format(
                                labelFormat,
                                weeklyLabelList.get(0),
                                weeklyLabelList.get(weeklyLabelList.size() - 1)
                        )
                );
            }

            List<String> labelList = new ArrayList<>();
            for (int i = 0; i < weeklyLabelList.size(); i += 7) {
                labelList.add(
                        String.format(
                                labelFormat,
                                weeklyLabelList.get(i),
                                weeklyLabelList.get(i + 6)
                        )
                );
            }

            return labelList;
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
    public record Response(
            List<StatisticsElement> registeredUsersStatisticsElementList,
            List<StatisticsElement> deletedUsersStatisticsElementList
    ) {

        @Builder
        public record StatisticsElement(
              int sequence,
              String label,
              int count
        ) { }

        public static Response of(
                final List<StatisticsElement> registeredUsersStatisticsElementList,
                final List<StatisticsElement> deletedUsersStatisticsElementList
        ) {
            return Response.builder()
                    .registeredUsersStatisticsElementList(registeredUsersStatisticsElementList)
                    .deletedUsersStatisticsElementList(deletedUsersStatisticsElementList)
                    .build();
        }
    }
}
