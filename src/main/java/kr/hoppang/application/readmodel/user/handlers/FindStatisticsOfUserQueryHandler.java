package kr.hoppang.application.readmodel.user.handlers;

import static kr.hoppang.util.common.DayUtil.getKorDayOfWeek;
import static kr.hoppang.util.common.DayUtil.getKorMonth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindStatisticsOfUserQuery;
import kr.hoppang.application.readmodel.user.queries.FindStatisticsOfUserQuery.Request;
import kr.hoppang.application.readmodel.user.queries.FindStatisticsOfUserQuery.Response;
import kr.hoppang.application.readmodel.user.queries.FindStatisticsOfUserQuery.Response.StatisticsElement;
import kr.hoppang.domain.statistics.SearchPeriodType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindStatisticsOfUserQueryHandler implements IQueryHandler<FindStatisticsOfUserQuery.Request, FindStatisticsOfUserQuery.Response> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Response handle(final Request query) {

        List<User> allRegisteredUsersWithOutAnyFiltered = userRepository.findAllRegisteredUsersBetween(query.getStartTimeForSearch(),
                query.getEndTimeForSearch());

        Map<LocalDate, List<User>> registeredUsersByDate = mapUsersToCreation(
                query.getStatisticsRangeList(),
                allRegisteredUsersWithOutAnyFiltered,
                query.searchPeriodType()
        );

        List<User> allDeletedUsersWithOutAnyFiltered = userRepository.findAllDeletedUsersBetween(query.getStartTimeForSearch(),
                query.getEndTimeForSearch());

        Map<LocalDate, List<User>> deletedUsersByDate = mapUsersToDeletion(
                query.getStatisticsRangeList(),
                allDeletedUsersWithOutAnyFiltered,
                query.searchPeriodType()
        );

        return Response.of(
                getStatisticsList(
                        query.searchPeriodType(),
                        query.searchPeriodValue(),
                        LocalDateTime.now(),
                        registeredUsersByDate
                ),
                getStatisticsList(
                        query.searchPeriodType(),
                        query.searchPeriodValue(),
                        LocalDateTime.now(),
                        deletedUsersByDate
                )
        );
    }

    private Map<LocalDate, List<User>> mapUsersToCreation(
            final List<List<LocalDateTime>> statisticsLabelRangeList,
            final List<User> userList,
            final SearchPeriodType searchPeriodType
    ) {

        if (statisticsLabelRangeList.isEmpty()) {
            return new HashMap<>();
        }

        Map<LocalDate, List<User>> usersByDate = new HashMap<>();

        List<User> filteredUsers = new ArrayList<>();

        // @TODO 주간, 월간은 차근차근 생각 해보면서 하기
        for (List<LocalDateTime> dateRange : statisticsLabelRangeList) {
//            if (searchPeriodType.equals(SearchPeriodType.WEEKLY)) {
//                LocalDateTime from = dateRange.get(1);
//                LocalDateTime to = dateRange.get(0);
//                List<User> weeklyFilteredUsers = userList.stream()
//                        .filter(user -> isBetween(from, user.getCreatedAt(), to))
//                        .toList();
//
//                filteredUsers.addAll(weeklyFilteredUsers); // 기존 리스트에 추가
//
//                for (LocalDateTime targetDate : dateRange) {
//                    LocalDate key = targetDate.toLocalDate();
//                    usersByDate.computeIfAbsent(key, k -> new ArrayList<>()).addAll(weeklyFilteredUsers);
//                }
//                continue;
//            }

            for (LocalDateTime targetDate : dateRange) {
                LocalDate key = targetDate.toLocalDate();

//                // 특정 날짜에 해당하는 유저 리스트 가져오기
//                if (searchPeriodType.equals(SearchPeriodType.MONTH)) {
//
//                }

                if (searchPeriodType.equals(SearchPeriodType.DAILY)) {

                    filteredUsers = userList.stream()
                            .filter(user -> isEqual(user.getCreatedAt(), targetDate))
                            .toList();
                }

                // 기존 리스트에 추가 (중복 방지)
                usersByDate.computeIfAbsent(key, k -> new ArrayList<>()).addAll(filteredUsers);
            }
        }

        return usersByDate;
    }

    private Map<LocalDate, List<User>> mapUsersToDeletion(
            final List<List<LocalDateTime>> statisticsLabelRangeList,
            final List<User> userList,
            final SearchPeriodType searchPeriodType
    ) {

        if (statisticsLabelRangeList.isEmpty()) {
            return new HashMap<>();
        }

        Map<LocalDate, List<User>> usersByDate = new HashMap<>();

        List<User> filteredUsers = new ArrayList<>();

        // @TODO 주간, 월간은 차근차근 생각 해보면서 하기
        for (List<LocalDateTime> dateRange : statisticsLabelRangeList) {
//            if (searchPeriodType.equals(SearchPeriodType.WEEKLY)) {
//                LocalDateTime from = dateRange.get(1);
//                LocalDateTime to = dateRange.get(0);
//                List<User> weeklyFilteredUsers = userList.stream()
//                        .filter(user -> isBetween(from, user.getCreatedAt(), to))
//                        .toList();
//
//                filteredUsers.addAll(weeklyFilteredUsers); // 기존 리스트에 추가
//
//                for (LocalDateTime targetDate : dateRange) {
//                    LocalDate key = targetDate.toLocalDate();
//                    usersByDate.computeIfAbsent(key, k -> new ArrayList<>()).addAll(weeklyFilteredUsers);
//                }
//                continue;
//            }

            for (LocalDateTime targetDate : dateRange) {
                LocalDate key = targetDate.toLocalDate();

//                // 특정 날짜에 해당하는 유저 리스트 가져오기
//                if (searchPeriodType.equals(SearchPeriodType.MONTH)) {
//
//                }

                if (searchPeriodType.equals(SearchPeriodType.DAILY)) {

                    filteredUsers = userList.stream()
                            .filter(User::isDeleted)
                            .filter(user -> isEqual(user.getDeletedAt(), targetDate))
                            .toList();
                }

                // 기존 리스트에 추가 (중복 방지)
                usersByDate.computeIfAbsent(key, k -> new ArrayList<>()).addAll(filteredUsers);
            }
        }

        return usersByDate;
    }

    private boolean isBetween(final LocalDateTime from, final LocalDateTime target, final LocalDateTime to) {
        return !target.isBefore(from) && !target.isAfter(to); // 경계값 포함
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
