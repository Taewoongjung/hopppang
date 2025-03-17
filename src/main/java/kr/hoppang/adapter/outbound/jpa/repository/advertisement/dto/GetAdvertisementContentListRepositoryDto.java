package kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record GetAdvertisementContentListRepositoryDto() {

    @Builder
    public record Req(
            List<String> advIdList,
            boolean isOnAir,
            String advChannel,
            int limit,
            int offset
    ) { }

    @Builder
    public record Res(
            Long advertisementId,
            String advId,
            String advChannel,
            LocalDateTime startAt,
            LocalDateTime endAt,
            Long publisherId,
            String publisherName,
            String memo
    ) { }
}
