package kr.hoppang.domain.statistics.repository.dto;

import java.time.LocalDateTime;
import kr.hoppang.domain.statistics.EntryPageType;
import lombok.Builder;

@Builder
public record CreateUserTrafficSourceInfoRepositoryDto(
        Long advertisementContentId,
        EntryPageType entryPageType,
        String referrer,
        String browser,
        Integer stayDuration,
        LocalDateTime visitedAt
) {

}
