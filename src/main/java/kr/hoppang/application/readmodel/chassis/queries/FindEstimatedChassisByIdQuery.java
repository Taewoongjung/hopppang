package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;
import lombok.Builder;

public record FindEstimatedChassisByIdQuery() {

    @Builder
    public record Req(
            long estimatedId,
            long queriedUserId
    ) implements IQuery { }

    @Builder
    public record Res(
            ChassisEstimationInfo estimationInfo,
            ChassisDiscountEvent chassisDiscountEvent
    ) { }
}
