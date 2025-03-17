package kr.hoppang.adapter.inbound.statistics.admin.readmodel;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.statistics.admin.webdto.FindAdvertisementUrlsForStatisticsWebDtoV1;
import kr.hoppang.application.readmodel.advertisement.handlers.FindAdvertisementUrlsForStatisticsQueryHandler;
import kr.hoppang.application.readmodel.advertisement.queries.FindAdvertisementUrlsForStatisticsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin/statistics")
public class StatisticsAdminQueryController {

    private final FindAdvertisementUrlsForStatisticsQueryHandler findAdvertisementUrlsForStatisticsQueryHandler;


    @GetMapping(
            value = "/advertisements/urls",
            produces = APPLICATION_JSON_VALUE
    )
    public FindAdvertisementUrlsForStatisticsWebDtoV1.Res findAdvertisementUrlsForStatistics(
            @Valid FindAdvertisementUrlsForStatisticsWebDtoV1.Req req
    ) {

        return
                FindAdvertisementUrlsForStatisticsWebDtoV1.Res.of(
                        findAdvertisementUrlsForStatisticsQueryHandler.handle(
                                FindAdvertisementUrlsForStatisticsQuery.Req.of(
                                        req.advIdList(),
                                        req.limit(),
                                        req.offset(),
                                        req.isOnAir(),
                                        req.advChannel()
                                )
                        )
                );
    }
}
