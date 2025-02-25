package kr.hoppang.application.readmodel.chassis.handlers;

import static kr.hoppang.util.common.DayUtil.getKorDayOfWeek;
import static kr.hoppang.util.common.DayUtil.getKorMonth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.application.readmodel.user.queries.GetStatisticsOfUserQuery.Response.StatisticsElement;
import kr.hoppang.domain.statistics.SearchPeriodType;
import kr.hoppang.domain.user.User;
import org.junit.jupiter.api.Test;

class StatisticsTestClass {

    @Test
    public void NASDAQ() {
        SearchPeriodType searchPeriodType = SearchPeriodType.MONTH;
        int searchPeriodValue = 3;
        LocalDateTime now = LocalDateTime.of(2025,2,17,0,0);

//        System.out.println(getStatisticsRangeList(searchPeriodType, searchPeriodValue, now));

//        System.out.println(extractNumbersFromRange(aa.get(0)));

        LocalDate comp1 = LocalDate.of(2025,2,17);
        LocalDate comp2 = LocalDate.of(2025, 2, 17);

        System.out.println(comp1.equals(comp2));
    }

    private List<Integer> extractNumbersFromRange(final String range) {
        // 괄호 제거 및 공백 제거 후 "~" 기준으로 나누기
        String[] parts = range.replaceAll("[()]", "").trim().split(" ~ ");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format: " + range);
        }

        try {
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());
            return List.of(start, end);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in range: " + range, e);
        }
    }

    private List<List<LocalDateTime>> getStatisticsRangeList(
            SearchPeriodType searchPeriodType,
            int searchPeriodValue,
            LocalDateTime now
    ) {

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


    @Test
    void as() {
        SearchPeriodType searchPeriodType = SearchPeriodType.DAILY;
        int searchPeriodValue = 1;
        LocalDateTime now = LocalDateTime.of(2025,2,6,0,0);

        Map<LocalDate, List<User>> usersByDate = new HashMap<>();

        // 특정 날짜에 몇 명의 사용자 데이터를 추가
        usersByDate.put(LocalDate.of(2025, 1, 11), Arrays.asList(
                User.of(1L, "User1", LocalDateTime.of(2025, 1, 11, 10, 0)),
                User.of(2L, "User2", LocalDateTime.of(2025, 1, 11, 11, 0))
        ));

        usersByDate.put(LocalDate.of(2025, 2, 1), Arrays.asList(
                User.of(1L, "User1", LocalDateTime.of(2025, 2, 1, 10, 0)),
                User.of(2L, "User2", LocalDateTime.of(2025, 2, 1, 11, 0))
        ));

        usersByDate.put(LocalDate.of(2025, 2, 3), Collections.singletonList(
                User.of(3L, "User3", LocalDateTime.of(2025, 2, 3, 9, 30))
        ));

        usersByDate.put(LocalDate.of(2025, 2, 5), Arrays.asList(
                User.of(4L, "User4", LocalDateTime.of(2025, 2, 5, 14, 0)),
                User.of(5L, "User5", LocalDateTime.of(2025, 2, 5, 15, 0)),
                User.of(6L, "User6", LocalDateTime.of(2025, 2, 5, 16, 0))
        ));

        usersByDate.put(LocalDate.of(2025, 2, 10), Collections.singletonList(
                User.of(7L, "User7", LocalDateTime.of(2025, 2, 10, 8, 45))
        ));

        usersByDate.put(LocalDate.of(2025, 2, 28), Arrays.asList(
                User.of(8L, "User8", LocalDateTime.of(2025, 2, 28, 18, 0)),
                User.of(9L, "User9", LocalDateTime.of(2025, 2, 28, 19, 0))
        ));
        System.out.println(getStatisticsList(searchPeriodType, searchPeriodValue, now, usersByDate));

    }

    public List<StatisticsElement> getStatisticsList(
            SearchPeriodType searchPeriodType,
            int searchPeriodValue,
            LocalDateTime now,
            Map<LocalDate, List<User>> usersByDate
    ) {

        List<StatisticsElement> statisticsList = new ArrayList<>();

        if (searchPeriodType.equals(SearchPeriodType.DAILY)) {
            for (int i = 0; i < searchPeriodValue; i++) {
                LocalDateTime targetDate = now.minusDays(i);
                int dayOfWeekNum = targetDate.getDayOfWeek().getValue();
                String dayOfWeek = getKorDayOfWeek(dayOfWeekNum);

                statisticsList.add(
                        StatisticsElement.builder()
                                .sequence(i)
                                .label(dayOfWeek)
                                .count(
                                        usersByDate.getOrDefault(
                                                targetDate.toLocalDate(),
                                                List.of()
                                        ).size()
                                )
                                .build()
                );
            }

            return statisticsList;
        }

        if (searchPeriodType.equals(SearchPeriodType.WEEKLY)) {
            for (int i = 0; i < searchPeriodValue; i++) {
                List<String> weeklyLabels = new ArrayList<>();
                List<LocalDateTime> weekDates = new ArrayList<>();

                LocalDateTime weekStart = now.minusWeeks(i).with(java.time.DayOfWeek.MONDAY);
                LocalDateTime weekEnd = weekStart.plusDays(6);

                for (int j = 0; j < 7; j++) {
                    LocalDateTime targetDate = weekStart.plusDays(j);
                    weekDates.add(targetDate);
                    weeklyLabels.add(String.valueOf(targetDate.getDayOfMonth()));
                }

                statisticsList.add(
                        StatisticsElement.builder()
                                .sequence(i)
                                .label(getStatisticsWeeklyLabelList(weeklyLabels))
                                .count(
                                        weekDates.stream()
                                                .mapToInt(targetDate ->
                                                        usersByDate.getOrDefault(
                                                                targetDate.toLocalDate(),
                                                                List.of()
                                                        ).size()
                                                )
                                                .sum()
                                )
                                .build()
                );
            }
        }

        if (searchPeriodType.equals(SearchPeriodType.MONTH)) {
            for (int i = 0; i < searchPeriodValue; i++) {
                LocalDateTime targetDate = now.minusMonths(i);
                int month = targetDate.getMonthValue();

                statisticsList.add(
                        StatisticsElement.builder()
                                .sequence(i)
                                .label(getKorMonth(month))
                                .count(
                                        userCountOfTargetMonth(
                                                usersByDate, targetDate
                                        )
                                )
                                .build()
                );
            }
        }

        return statisticsList;
    }

    private String getStatisticsWeeklyLabelList(final List<String> weeklyLabelList) {
        if (weeklyLabelList.isEmpty()) {
            return "(N/A)";
        }

        List<Integer> sortedDates = weeklyLabelList.stream()
                .map(Integer::parseInt)
                .sorted()
                .toList();

        return String.format("(%d ~ %d)", sortedDates.get(0), sortedDates.get(sortedDates.size() - 1));
    }

    private int userCountOfTargetMonth(
            final Map<LocalDate, List<User>> usersByDate,
            final LocalDateTime targetDate
    ) {
        List<LocalDate> listOfTheMonth = getDaysOfMonth(targetDate);

        return listOfTheMonth.stream()
                .mapToInt(key -> usersByDate.getOrDefault(key, List.of()).size())
                .sum();

    }

    private List<LocalDate> getDaysOfMonth(final LocalDateTime targetDate) {
        LocalDate firstDay = targetDate.toLocalDate().withDayOfMonth(1); // 해당 월의 1일
        int lastDay = targetDate.toLocalDate().lengthOfMonth(); // 마지막 날짜 구하기

        List<LocalDate> daysOfMonth = new ArrayList<>();
        for (int i = 0; i < lastDay; i++) {
            daysOfMonth.add(firstDay.plusDays(i));
        }

        return daysOfMonth;
    }
}
