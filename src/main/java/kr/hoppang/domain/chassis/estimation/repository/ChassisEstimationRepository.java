package kr.hoppang.domain.chassis.estimation.repository;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto.FindChassisEstimationInfoByUserIdRepositoryDto;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;
import kr.hoppang.domain.chassis.estimation.repository.dto.FindChassisEstimationInfosResult;
import org.springframework.data.domain.Pageable;

public interface ChassisEstimationRepository {

    List<FindChassisEstimationInfosResult> findChassisEstimationInfosBy(
            final List<Long> estimationId,
            final CompanyType companyType,
            final ChassisType chassisType,
            final LocalDateTime startTime,
            final LocalDateTime endTime,
            final int limit, final int offset
    );

    long registerChassisEstimation(final ChassisEstimationInfo ChassisEstimationInfo,
            final List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList);

    ChassisEstimationInfo findChassisEstimationInfoById(final long estimationId);

    FindChassisEstimationInfoByUserIdRepositoryDto.Response findChassisEstimationInfoByUserId(
            final long userId,
            final Pageable pageable,
            final Long lastEstimationId);

    long findAllEstimationsProvidedToCustomer();

    List<ChassisEstimationInfo> findAllRegisteredChassisEstimationsBetween(final LocalDateTime start,
            final LocalDateTime end);
}
