package kr.hoppang.adapter.inbound.statistics.admin.webdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.time.LocalDateTime;

public record AddAdvertisementUrlsForStatisticsWebDtoV1() {

    public record Req(

            @JsonProperty(value = "advId")
            @JsonPropertyDescription("광고의 유니크값")
            String advId,

            @JsonProperty(value = "targetPlatform")
            @JsonPropertyDescription("광고가 노출되는 플랫폼 (예: WEB, APP, MOBILE_WEB)")
            String targetPlatform,

            @JsonProperty(value = "memo")
            @JsonPropertyDescription("광고 대상이 된 플랫폼과 상세 카테고리를 설명하고, 광고 URL에 대한 메모")
            String memo,

            @JsonProperty(value = "startedAt")
            @JsonPropertyDescription("광고 시작 시간")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime startedAt
    ) { }
}
