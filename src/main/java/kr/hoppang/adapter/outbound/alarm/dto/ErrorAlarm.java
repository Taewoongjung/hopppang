package kr.hoppang.adapter.outbound.alarm.dto;

public record ErrorAlarm(String errorTitle,
                         String queryParam,
                         String requestedBody,
                         String errorMsg) {

}
