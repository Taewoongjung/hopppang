package kr.hoppang.adapter.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VersatileUtil {

    public static LocalDateTime convertStringToLocalDateTime(final String target) {
        LocalDate date = LocalDate.parse(target, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return date.atStartOfDay(); // 시간을 00:00:00으로 설정
    }
}