package kr.hoppang.application.readmodel.user.queries;

import lombok.Builder;

public record FindUserInboundStatisticsQuery() {

    @Builder
    public record Res(
            String androidPercentile,
            String iosPercentile
    ) {

    }
}
