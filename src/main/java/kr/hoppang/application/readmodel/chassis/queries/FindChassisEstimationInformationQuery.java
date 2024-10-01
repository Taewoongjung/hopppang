package kr.hoppang.application.readmodel.chassis.queries;

import java.time.LocalDateTime;
import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public record FindChassisEstimationInformationQuery(Long estimationId,
                                                    CompanyType companyType,
                                                    ChassisType chassisType,
                                                    LocalDateTime startTime,
                                                    LocalDateTime endTime,
                                                    int limit, int offset) implements IQuery {

}
