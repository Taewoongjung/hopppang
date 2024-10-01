package kr.hoppang.domain.chassis.estimation.repository;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;
import kr.hoppang.domain.chassis.estimation.repository.dto.FindChassisEstimationInfosResult;

public interface ChassisEstimationRepository {

    public List<FindChassisEstimationInfosResult> findChassisEstimationInfosBy(
            final Long estimationId,
            final CompanyType companyType,
            final ChassisType chassisType,
            final LocalDateTime startTime,
            final LocalDateTime endTime,
            final int limit, final int offset
    );

    public void registerChassisEstimation(final ChassisEstimationInfo ChassisEstimationInfo,
            final List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList);
}
