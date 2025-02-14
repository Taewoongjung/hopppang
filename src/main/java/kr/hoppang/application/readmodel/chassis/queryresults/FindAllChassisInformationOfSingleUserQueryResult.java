package kr.hoppang.application.readmodel.chassis.queryresults;

import java.util.List;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import lombok.Builder;

@Builder
public record FindAllChassisInformationOfSingleUserQueryResult(
        List<ChassisEstimationInfo> chassisEstimationInfoList,
        boolean isEndOfList
) {

}
