package kr.hoppang.adapter.outbound.jpa.repository.chassis.event.adapter;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.event.ChassisDiscountEventEntity;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.event.ChassisDiscountEventJpaRepository;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;
import kr.hoppang.domain.chassis.event.repository.ChassisDiscountEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChassisDiscountEventRepositoryAdapter implements ChassisDiscountEventRepository {

    private final ChassisDiscountEventJpaRepository chassisDiscountEventJpaRepository;

    @Override
    public List<ChassisDiscountEvent> findAllChassisDiscountEventByCompanyAndChassisType(
            CompanyType companyType,
            List<ChassisType> chassisTypeList
    ) {

        List<ChassisDiscountEventEntity> chassisDiscountEventEntityList = chassisDiscountEventJpaRepository.findByCompanyTypeAndChassisTypeAndIsStillOnDiscount(
                companyType,
                chassisTypeList
        );

        return chassisDiscountEventEntityList.stream()
                .map(ChassisDiscountEventEntity::toPojo)
                .toList();
    }
}
