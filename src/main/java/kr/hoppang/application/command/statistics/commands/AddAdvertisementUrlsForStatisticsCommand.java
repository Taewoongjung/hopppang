package kr.hoppang.application.command.statistics.commands;

import java.time.LocalDateTime;
import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

public record AddAdvertisementUrlsForStatisticsCommand() {

    @Builder
    public record Req(
            String advId,
            String targetPlatform,
            String memo,
            LocalDateTime startedAt,
            Long publisherId
    ) implements ICommand { }
}
