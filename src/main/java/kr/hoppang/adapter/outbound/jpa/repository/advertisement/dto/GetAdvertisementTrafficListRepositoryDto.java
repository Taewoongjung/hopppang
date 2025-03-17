package kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import kr.hoppang.domain.statistics.EntryPageType;
import lombok.Builder;

public record GetAdvertisementTrafficListRepositoryDto() {

    @Builder
    public record Req(
            Set<Long> advertisementIdList,
            List<String> advIdList,
            boolean isOnAir,
            String advChannel
    ) { }

    @Builder
    public record Res(
            String advId,
            String advChannel,
            EntryPageType entryPageType,
            String referrer,
            String browser,
            Integer stayDuration,
            LocalDateTime visitedAt
    ) { }
}
