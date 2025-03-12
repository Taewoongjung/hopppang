package kr.hoppang.adapter.inbound.statistics.command.webdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import kr.hoppang.domain.statistics.EntryPageType;

public record AddStatisticsInfoFromNewEntranceOfLandingPageWebDtoV1() {

    public record Request(

            String advId,

            String referrer,

            String browser,

            Integer stayDuration,

            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime visitedAt
    ) {

        public EntryPageType getEntryPageType() {
            return EntryPageType.LANDING_V1;
        }
    }
}
