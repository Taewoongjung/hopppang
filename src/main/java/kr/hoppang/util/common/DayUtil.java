package kr.hoppang.util.common;

public class DayUtil {

    public static String getKorDayOfWeek(final int dayOfWeek) {

        String korDayOfWeek = "";

        switch (dayOfWeek) {
            case 1:
                korDayOfWeek = "일";
            case 2:
                korDayOfWeek = "월";
            case 3:
                korDayOfWeek = "화";
            case 4:
                korDayOfWeek = "수";
            case 5:
                korDayOfWeek = "목";
            case 6:
                korDayOfWeek = "금";
            case 7:
                korDayOfWeek = "토";
        }
        return korDayOfWeek;
    }
}
