package kr.hoppang.adapter.inbound.user.admin.webdto;

import jakarta.validation.constraints.Min;
import java.util.List;
import kr.hoppang.application.readmodel.user.queries.GetStatisticsOfUserQuery;
import kr.hoppang.application.readmodel.user.queries.GetStatisticsOfUserQuery.Response.StatisticsElement;
import kr.hoppang.domain.statistics.SearchPeriodType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.web.bind.annotation.BindParam;

public record SearchUserStatisticsWebDtoV1() {

    public record Request(

            @NonNull
            @BindParam(value = "searchPeriodType")
            SearchPeriodType searchPeriodType,
            // @TODO
            //    DAILY : 오늘 기준 - 7 (디폴트)
            //    WEEKLY : 이번주 기준 - 3 (디폴트)
            //    MONTH : 이번달 기준 -5 (디폴트)

            @NonNull
            @Min(
                    value = 1,
                    message = "searchPeriodValue {value} 미만일 수 없습니다."
            )
            @BindParam(value = "searchPeriodValue")
            Integer searchPeriodValue
            // *일/주/월* 선택 가능함.
    ) { }

    @Builder(access = AccessLevel.PRIVATE)
    public record Response(
            List<StatisticsElement> statisticsElements
    ) {

        public static Response of(final GetStatisticsOfUserQuery.Response responseFromHandler) {
            return Response.builder()
                    .statisticsElements(responseFromHandler.statisticsElements())
                    .build();
        }
    }
}
