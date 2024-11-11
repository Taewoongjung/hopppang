package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;

public record InquiryChassisEstimationCommand(long estimationId) implements IQuery {

}
