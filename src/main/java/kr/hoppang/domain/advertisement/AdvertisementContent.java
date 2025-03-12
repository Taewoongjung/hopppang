package kr.hoppang.domain.advertisement;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AdvertisementContent {

    private final Long id;

    private final String advId;

    private final String advChannel;

    private final LocalDateTime startAt;

    private final LocalDateTime endAt;

    private final Long publisherId;

    private final String memo;

    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    private AdvertisementContent(
            final Long id,
            final String advId,
            final String advChannel,
            final LocalDateTime startAt,
            final LocalDateTime endAt,
            final Long publisherId,
            final String memo,
            final LocalDateTime createdAt,
            final LocalDateTime lastModifiedAt
    ) {

        this.id = id;
        this.advId = advId;
        this.advChannel = advChannel;
        this.startAt = startAt;
        this.endAt = endAt;
        this.publisherId = publisherId;
        this.memo = memo;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    // 조회
    public static AdvertisementContent of(
            final Long id,
            final String advId,
            final String advChannel,
            final LocalDateTime startAt,
            final LocalDateTime endAt,
            final Long publisherId,
            final String memo,
            final LocalDateTime createdAt,
            final LocalDateTime lastModifiedAt
    ) {
        return new AdvertisementContent(
                id,
                advId,
                advChannel,
                startAt,
                endAt,
                publisherId,
                memo,
                createdAt, lastModifiedAt
        );
    }

    // 생성
    public static AdvertisementContent of(
            final String advId,
            final String advChannel,
            final LocalDateTime startAt,
            final LocalDateTime endAt,
            final Long publisherId,
            final String memo
    ) {
        return new AdvertisementContent(
                null,
                advId,
                advChannel,
                startAt,
                endAt,
                publisherId,
                memo,
                null, null
        );
    }
}
