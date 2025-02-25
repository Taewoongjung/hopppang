package kr.hoppang.adapter.inbound.chassis.admin.webdto;

import jakarta.validation.constraints.Min;
import java.util.List;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedStatisticsQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedStatisticsQuery.Res.StatisticsElement;
import kr.hoppang.domain.statistics.SearchPeriodType;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.web.bind.annotation.BindParam;

public record GetEstimatedStatisticsWebDtoV1() {

    public record Req(
            @NonNull
            @BindParam(value = "searchPeriodType")
            SearchPeriodType searchPeriodType,

            @NonNull
            @Min(
                    value = 1,
                    message = "searchPeriodValue {value} 미만일 수 없습니다."
            )
            @BindParam(value = "searchPeriodValue")
            Integer searchPeriodValue
    ) { }

    @Builder
    public record Res(
            List<StatisticsElement> registeredChassisEstimationsStatisticsElementList
    ) {

        public static Res of(FindEstimatedStatisticsQuery.Res responseFromQueryHandler) {
            return Res.builder()
                    .registeredChassisEstimationsStatisticsElementList(
                            responseFromQueryHandler.registeredChassisEstimationsStatisticsElementList())
                    .build();
        }
    }
}
