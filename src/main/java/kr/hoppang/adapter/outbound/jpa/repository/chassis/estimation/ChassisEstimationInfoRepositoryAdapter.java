package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation;


import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationAddressToEntity;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationAddressEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@RequiredArgsConstructor
public class ChassisEstimationInfoRepositoryAdapter implements ChassisEstimationRepository {

    private final ChassisEstimationInfoJpaRepository chassisEstimationJpaRepository;
    private final ChassisEstimationAddressJpaRepository chassisEstimationAddressJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChassisEstimationInfo> findChassisEstimationInfosBy() {

        return null;
    }

    @Override
    @Transactional
    public void registerChassisEstimation(
            final List<ChassisEstimationInfo> chassisEstimationInfoList) {

        // address id 설정 start
        if (chassisEstimationInfoList.get(0).getChassisEstimationAddress() != null) {
            ChassisEstimationAddressEntity chassisEstimationAddressEntity =
                    chassisEstimationAddressJpaRepository.save(
                            chassisEstimationAddressToEntity(
                                    chassisEstimationInfoList.get(0).getChassisEstimationAddress()
                            )
                    );

            chassisEstimationInfoList.forEach(
                    e -> e.getChassisEstimationAddress().setId(chassisEstimationAddressEntity.getId()));
        }
        // address id 설정 end

        List<ChassisEstimationInfoEntity> chassisEstimationInfoEntityList = chassisEstimationInfoList.stream()
                .map(ChassisEstimationConverter::chassisEstimationInfoToEntity)
                .toList();

        chassisEstimationJpaRepository.saveAll(chassisEstimationInfoEntityList);
    }
}
