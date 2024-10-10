package kr.hoppang.adapter.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class VersatileUtil {

    public static LocalDateTime convertStringToLocalDateTime(final String target) {
        LocalDate date = LocalDate.parse(target, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return date.atStartOfDay(); // 시간을 00:00:00으로 설정
    }

    public static LocalDateTime convertStringToLocalDateTime2(final String target) {
        return LocalDateTime.parse(target, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static Date convertLocalDateTimeToDate(final LocalDateTime accessTokenExpireTime) {
        return Date.from(accessTokenExpireTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
