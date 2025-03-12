package kr.hoppang.application.command.statistics.event;

import java.time.LocalDateTime;
import kr.hoppang.domain.statistics.EntryPageType;
import lombok.Builder;

@Builder
public record UserTrafficSourceEvent(
        String advId,
        EntryPageType entryPageType,
        String referrer,
        String browser,
        Integer stayDuration,
        LocalDateTime visitedAt
) { }
