package kr.hoppang.adapter.inbound.statistics.admin.command;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import kr.hoppang.adapter.inbound.statistics.admin.webdto.AddAdvertisementUrlsForStatisticsWebDtoV1;
import kr.hoppang.adapter.inbound.user.AuthenticationUserId;
import kr.hoppang.application.command.statistics.commands.AddAdvertisementUrlsForStatisticsCommand;
import kr.hoppang.application.command.statistics.handlers.AddAdvertisementUrlsForStatisticsCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin/statistics")
public class StatisticsAdminCommandController {

    private final AddAdvertisementUrlsForStatisticsCommandHandler addAdvertisementUrlsForStatisticsCommandHandler;


    @PostMapping(
            value = "/advertisements/urls",
            consumes = APPLICATION_JSON_VALUE
    )
    public void addAdvertisementUrlsForStatistics(
            @RequestBody AddAdvertisementUrlsForStatisticsWebDtoV1.Req req,
            @AuthenticationUserId Long userId
    ) {

        addAdvertisementUrlsForStatisticsCommandHandler.handle(
                AddAdvertisementUrlsForStatisticsCommand.Req.builder()
                        .advId(req.advId())
                        .targetPlatform(req.targetPlatform())
                        .memo(req.memo())
                        .startedAt(req.startedAt())
                        .publisherId(userId)
                        .build()
        );
    }
}
