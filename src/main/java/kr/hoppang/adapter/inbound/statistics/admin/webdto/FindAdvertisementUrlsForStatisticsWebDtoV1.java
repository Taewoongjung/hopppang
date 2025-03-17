package kr.hoppang.adapter.inbound.statistics.admin.webdto;

import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.application.readmodel.advertisement.queries.FindAdvertisementUrlsForStatisticsQuery;
import lombok.AccessLevel;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.BindParam;

public record FindAdvertisementUrlsForStatisticsWebDtoV1() {

    public record Req(

            @Min(
                    value = 0,
                    message = "offset는 {value} 미만일 수 없습니다."
            )
            @BindParam(value = "limit")
            int limit,

            @Range(
                    min = 1,
                    max = 1000,
                    message = "limit는 최소 {min}개 최대 {max}개를 초과할 수 없습니다."
            )
            @BindParam(value = "offset")
            int offset,

            @BindParam(value = "advIdList")
            List<String> advIdList,

            @BindParam(value = "isOnAir")
            Boolean isOnAir,

            @BindParam(value = "advChannel")
            String advChannel
    ) { }

    public record Res(List<AdvContentWithClickCount> advContentWithClickCountList) {

        @Builder(access = AccessLevel.PRIVATE)
        private record AdvContentWithClickCount(
                String advId,
                String advChannel,
                LocalDateTime startAt,
                LocalDateTime endAt,
                Long publisherId,
                String publisherName,
                String memo,
                int clickCount
        ) { }

        public static Res of(
                final List<FindAdvertisementUrlsForStatisticsQuery.Res> fromHandlerResDto) {

            return new Res(
                    fromHandlerResDto.stream()
                            .map(e ->
                                    AdvContentWithClickCount.builder()
                                            .advId(e.advId())
                                            .advChannel(e.advChannel())
                                            .startAt(e.startAt())
                                            .endAt(e.endAt())
                                            .publisherId(e.publisherId())
                                            .publisherName(e.publisherName())
                                            .memo(e.memo())
                                            .clickCount(e.clickCount())
                                            .build()
                            ).toList()
            );
        }
    }
}
