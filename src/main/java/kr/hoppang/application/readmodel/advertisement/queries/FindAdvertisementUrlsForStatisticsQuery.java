package kr.hoppang.application.readmodel.advertisement.queries;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

public record FindAdvertisementUrlsForStatisticsQuery() {

    @Builder
    public record Req(
            List<String> advIdList,
            int limit,
            int offset,
            Boolean isOnAir,
            String advChannel
    ) implements IQuery {

        public static Req of(
                List<String> advIdList,
                int limit,
                int offset,
                Boolean isOnAir,
                String advChannel
        ) {
            return Req.builder()
                    .advIdList(advIdList)
                    .limit(limit)
                    .offset(offset)
                    .isOnAir(isOnAir)
                    .advChannel(advChannel)
                    .build();
        }
    }

    @Builder
    public record Res(
            String advId,
            String advChannel,
            LocalDateTime startAt,
            LocalDateTime endAt,
            Long publisherId,
            String publisherName,
            String memo,
            int clickCount
    ) { }
}
