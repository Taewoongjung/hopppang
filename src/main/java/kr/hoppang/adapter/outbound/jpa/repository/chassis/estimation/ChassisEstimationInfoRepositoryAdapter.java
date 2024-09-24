package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation;

import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationAddressToEntity;
import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationInfoToEntity;
import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationSizeInfoToEntity;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationAddressEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationSizeInfoEntity;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@RequiredArgsConstructor
public class ChassisEstimationInfoRepositoryAdapter implements ChassisEstimationRepository {

    private final ChassisEstimationInfoJpaRepository chassisEstimationJpaRepository;
    private final ChassisEstimationAddressJpaRepository chassisEstimationAddressJpaRepository;
    private final ChassisEstimationSizeInfoJpaRepository chassisEstimationSizeInfoJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChassisEstimationInfo> findChassisEstimationInfosBy() {

        return null;
    }

    @Override
    @Transactional
    public void registerChassisEstimation(final ChassisEstimationInfo chassisEstimationInfo,
            final List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList) {

        // address id 설정 start
        if (chassisEstimationInfo.getChassisEstimationAddress() != null) {
            ChassisEstimationAddressEntity chassisEstimationAddressEntity =
                    chassisEstimationAddressJpaRepository.save(
                            chassisEstimationAddressToEntity(
                                    chassisEstimationInfo.getChassisEstimationAddress()
                            )
                    );

            // 샤시 견적 주소 객체에 id 설정
            chassisEstimationInfo.getChassisEstimationAddress()
                    .setId(chassisEstimationAddressEntity.getId());
        }
        // address id 설정 end

        // ChassisEstimationInfo 디비 저장
        ChassisEstimationInfoEntity chassisEstimationInfoEntity =
                chassisEstimationJpaRepository.save(
                        chassisEstimationInfoToEntity(chassisEstimationInfo));

        List<ChassisEstimationSizeInfoEntity> chassisEstimationSizeInfoEntityList =
                chassisEstimationSizeInfoToEntity(chassisEstimationSizeInfoList, chassisEstimationInfoEntity.getId());

        chassisEstimationSizeInfoEntityList.forEach(
                e -> e.setChassisEstimationInfo(chassisEstimationInfoEntity));

        // ChassisEstimationSizeInfo 디비 저장
        chassisEstimationSizeInfoJpaRepository.saveAll(chassisEstimationSizeInfoEntityList);
    }
}
