package kr.hoppang.util.common;

public class DayUtil {

    public static String getKorDayOfWeek(final int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> "월";
            case 2 -> "화";
            case 3 -> "수";
            case 4 -> "목";
            case 5 -> "금";
            case 6 -> "토";
            case 7 -> "일";
            default -> "잘못된요일";
        };
    }

    public static String getKorMonth(final int month) {
        return switch (month) {
            case 1 -> "1월";
            case 2 -> "2월";
            case 3 -> "3월";
            case 4 -> "4월";
            case 5 -> "5월";
            case 6 -> "6월";
            case 7 -> "7월";
            case 8 -> "8월";
            case 9 -> "9월";
            case 10 -> "10월";
            case 11 -> "11월";
            case 12 -> "12월";
            default -> "잘못된월";
        };
    }
}
