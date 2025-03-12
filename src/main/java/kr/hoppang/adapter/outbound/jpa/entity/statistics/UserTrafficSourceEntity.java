package kr.hoppang.adapter.outbound.jpa.entity.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.domain.statistics.EntryPageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@ToString
@Table(name = "user_traffic_source")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTrafficSourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long advertisementContentId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "entry_page_type", nullable = false, columnDefinition = "char(10)")
    private EntryPageType entryPageType;

    private String referrer;

    private String browser;

    private Integer stayDuration;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "visited_at")
    private LocalDateTime visitedAt;


    private UserTrafficSourceEntity(
            final Long id,
            final Long advertisementContentId,
            final EntryPageType entryPageType,
            final String referrer,
            final String browser,
            final Integer stayDuration,
            final LocalDateTime visitedAt
    ) {
        this.id = id;
        this.advertisementContentId = advertisementContentId;
        this.entryPageType = entryPageType;
        this.referrer = referrer;
        this.browser = browser;
        this.stayDuration = stayDuration;
        this.visitedAt = visitedAt;
    }

    // 생성
    public static UserTrafficSourceEntity of(
            final Long advertisementContentId,
            final EntryPageType entryPageType,
            final String referrer,
            final String browser,
            final Integer stayDuration,
            final LocalDateTime visitedAt
    ) {
        return new UserTrafficSourceEntity(
                null,
                advertisementContentId,
                entryPageType,
                referrer,
                browser,
                stayDuration,
                visitedAt
        );
    }
}
