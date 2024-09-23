package kr.hoppang.domain.chassis.estimation.repository;

import java.util.List;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;

public interface ChassisEstimationRepository {

    public List<ChassisEstimationInfo> findChassisEstimationInfosBy(/* 여기에 필터 조건 추가 하기 */);

    public void registerChassisEstimation(final List<ChassisEstimationInfo> ChassisEstimationInfoList);
}
