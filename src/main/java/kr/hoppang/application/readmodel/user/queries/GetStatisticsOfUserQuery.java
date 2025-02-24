package kr.hoppang.application.readmodel.user.queries;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_REQUEST;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.common.DayUtil.getKorDayOfWeek;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.statistics.SearchPeriodType;
import lombok.AccessLevel;
import lombok.Builder;

public record GetStatisticsOfUserQuery() {

    public record Request(
            SearchPeriodType searchPeriodType,
            Integer searchPeriodValue
    ) implements IQuery {

        private static final LocalDateTime now = LocalDateTime.now();

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
                for(int i = 0; i < searchPeriodValue; i++) {
                    int dayOfWeekNum = now.getDayOfWeek().getValue() - i;
                    if (dayOfWeekNum < 0) {
                        dayOfWeekNum = 7;
                    }
                    String dayOfWeek = getKorDayOfWeek(dayOfWeekNum);
                    statisticsLabelList.add(dayOfWeek);
                }
            }

            if (searchPeriodType.equals(SearchPeriodType.WEEKLY)) {
                for (int i = 0; i < searchPeriodValue; i++) {
                    for (int j = 0; j < searchPeriodValue * 7; j++) {

                        int dayOfMonth = now.getDayOfMonth() - j;
                        if (dayOfMonth < 0) {
                            statisticsLabelList.add(
                                    String.valueOf(
                                            now.toLocalDate().minusMonths(i).lengthOfMonth()
                                    )
                            );
                        } else statisticsLabelList.add(String.valueOf(now.getDayOfMonth() - j));
                    }
                }
            }

            if (searchPeriodType.equals(SearchPeriodType.MONTH)) {
                for (int i = 0; i < searchPeriodValue; i++) {
                    int month = now.getMonthValue() - i;
                    if (month < 0) {
                        month = 12;
                    }
                    statisticsLabelList.add(String.valueOf(month));
                }
            }

            return statisticsLabelList;
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record Response(
            List<StatisticsElement> statisticsElements
    ) {

        @Builder
        public record StatisticsElement(
              int sequence,
              String label,
              int count
        ) { }

        public static Response of(final List<StatisticsElement> statisticsElementList) {
            return new Response(statisticsElementList);
        }
    }
}
