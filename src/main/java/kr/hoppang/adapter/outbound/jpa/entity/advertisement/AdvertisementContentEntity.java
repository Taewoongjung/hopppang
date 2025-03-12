package kr.hoppang.adapter.outbound.jpa.entity.advertisement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.advertisement.AdvertisementContent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "advertisement_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementContentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String advId;

    private String advChannel;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private Long publisherId;

    private String memo;


    private AdvertisementContentEntity(
            final Long id,
            final String advId,
            final String advChannel,
            final LocalDateTime startAt,
            final LocalDateTime endAt,
            final Long publisherId,
            final String memo
    ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.advId = advId;
        this.advChannel = advChannel;
        this.startAt = startAt;
        this.endAt = endAt;
        this.publisherId = publisherId;
        this.memo = memo;
    }

    // 생성
    public static AdvertisementContentEntity of(
            final String advId,
            final String advChannel,
            final LocalDateTime startAt,
            final LocalDateTime endAt,
            final Long publisherId,
            final String memo
    ) {
        return new AdvertisementContentEntity(
                null,
                advId,
                advChannel,
                startAt,
                endAt,
                publisherId,
                memo
        );
    }

    public AdvertisementContent toPojo() {
        return AdvertisementContent.of(
                this.id,
                this.advId,
                this.advChannel,
                this.startAt,
                this.endAt,
                this.publisherId,
                this.memo,
                this.getCreatedAt(),
                this.getLastModified()
        );
    }
}
