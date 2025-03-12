package kr.hoppang.adapter.inbound.statistics.command;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import kr.hoppang.adapter.inbound.statistics.command.webdto.AddStatisticsInfoFromNewEntranceOfLandingPageWebDtoV1;
import kr.hoppang.application.command.statistics.event.UserTrafficSourceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/statistics")
public class StatisticsCommandController {

    private final ApplicationEventPublisher eventPublisher;


    @PostMapping(
            value = "/pages/landing/user-inbound",
            consumes = APPLICATION_JSON_VALUE
    )
    public void addStatisticsInfoFromNewEntranceOfLandingPage(
            @RequestBody final AddStatisticsInfoFromNewEntranceOfLandingPageWebDtoV1.Request req
    ) {

        eventPublisher.publishEvent(
                UserTrafficSourceEvent.builder()
                        .advId(req.advId())
                        .entryPageType(req.getEntryPageType())
                        .referrer(req.referrer())
                        .browser(req.browser())
                        .stayDuration(req.stayDuration())
                        .visitedAt(req.visitedAt())
                        .build()
        );
    }
}
