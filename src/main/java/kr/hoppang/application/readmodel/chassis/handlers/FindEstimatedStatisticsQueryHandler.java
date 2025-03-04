package kr.hoppang.application.readmodel.chassis.handlers;

import static kr.hoppang.util.common.DayUtil.getKorDayOfWeek;
import static kr.hoppang.util.common.DayUtil.getKorMonth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedStatisticsQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedStatisticsQuery.Req;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedStatisticsQuery.Res;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedStatisticsQuery.Res.StatisticsElement;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import kr.hoppang.domain.statistics.SearchPeriodType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindEstimatedStatisticsQueryHandler implements IQueryHandler<FindEstimatedStatisticsQuery.Req, FindEstimatedStatisticsQuery.Res> {

    private final ChassisEstimationRepository chassisEstimationRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Res handle(final Req query) {

        List<ChassisEstimationInfo> allEstimationInfoList = chassisEstimationRepository.findAllRegisteredChassisEstimationsBetween(
                query.getStartTimeForSearch(), query.getEndTimeForSearch());

        Map<LocalDate, List<ChassisEstimationInfo>> estimatedChassisEstimationsByDate = mapEstimatedChassisEstimationsToCreation(
                query.getStatisticsRangeList(),
                allEstimationInfoList,
                query.searchPeriodType()
        );

        return Res.of(
                getStatisticsList(
                        query.searchPeriodType(),
                        query.searchPeriodValue(),
                        LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                        estimatedChassisEstimationsByDate
                )
        );
    }

    private Map<LocalDate, List<ChassisEstimationInfo>> mapEstimatedChassisEstimationsToCreation(
            final List<List<LocalDateTime>> statisticsLabelRangeList,
            final List<ChassisEstimationInfo> chassisEstimationInfoList,
            final SearchPeriodType searchPeriodType
    ) {

        if (statisticsLabelRangeList.isEmpty()) {
            return new HashMap<>();
        }

        Map<LocalDate, List<ChassisEstimationInfo>> estimationsByDate = new HashMap<>();

        List<ChassisEstimationInfo> filteredEstimations = new ArrayList<>();

        // @TODO 주간, 월간은 차근차근 생각 해보면서 하기
        for (List<LocalDateTime> dateRange : statisticsLabelRangeList) {

            for (LocalDateTime targetDate : dateRange) {
                LocalDate key = targetDate.toLocalDate();

//                // 특정 날짜에 해당하는 유저 리스트 가져오기
//                if (searchPeriodType.equals(SearchPeriodType.MONTH)) {
//
//                }

                if (searchPeriodType.equals(SearchPeriodType.DAILY)) {

                    filteredEstimations = chassisEstimationInfoList.stream()
                            .filter(user -> isEqual(user.getCreatedAt(), targetDate))
                            .toList();
                }

                // 기존 리스트에 추가 (중복 방지)
                estimationsByDate.computeIfAbsent(key, k -> new ArrayList<>())
                        .addAll(filteredEstimations);
            }
        }

        return estimationsByDate;
    }


    private boolean isEqual(final LocalDateTime target1, final LocalDateTime target2) {
        LocalDate comp1 = target1.toLocalDate();
        LocalDate comp2 = target2.toLocalDate();

        return comp1.equals(comp2);
    }

    private List<StatisticsElement> getStatisticsList(
            SearchPeriodType searchPeriodType,
            int searchPeriodValue,
            LocalDateTime now,
            Map<LocalDate, List<ChassisEstimationInfo>> usersByDate
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
                                .label(
                                        dayOfWeek + "(" +
                                                targetDate.getMonth().getValue() + "-" +
                                                targetDate.getDayOfMonth() +
                                                ")"
                                )
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
                                        chassisEstimationCountOfTargetMonth(
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

    private int chassisEstimationCountOfTargetMonth(
            final Map<LocalDate, List<ChassisEstimationInfo>> usersByDate,
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
