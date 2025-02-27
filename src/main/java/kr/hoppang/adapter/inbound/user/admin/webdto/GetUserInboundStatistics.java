package kr.hoppang.adapter.inbound.user.admin.webdto;

import kr.hoppang.application.readmodel.user.queries.FindUserInboundStatisticsQuery;

public record GetUserInboundStatistics() {

    public record Res(
            String androidPercentile,
            String iosPercentile
    ) {

        public static GetUserInboundStatistics.Res of(
                final FindUserInboundStatisticsQuery.Res responseFromHandler) {

            return new GetUserInboundStatistics.Res(
                    responseFromHandler.androidPercentile(),
                    responseFromHandler.iosPercentile()
            );
        }
    }
}
