package kr.hoppang.domain.statistics;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserTrafficSource {

    private final Long id;
    private final Long advertisementContentId;
    private final EntryPageType entryPageType;
    private final String referrer;
    private final String browser;
    private final Integer stayDuration;
    private final LocalDateTime visitedAt;


    private UserTrafficSource(
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
    public static UserTrafficSource of(
            final Long advertisementContentId,
            final EntryPageType entryPageType,
            final String referrer,
            final String browser,
            final Integer stayDuration,
            final LocalDateTime visitedAt
    ) {
        return new UserTrafficSource(
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
