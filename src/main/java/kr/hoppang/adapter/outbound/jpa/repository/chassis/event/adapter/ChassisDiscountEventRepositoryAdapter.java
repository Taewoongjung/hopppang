package kr.hoppang.adapter.outbound.jpa.repository.chassis.event.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.event.ChassisDiscountEventEntity;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.event.ChassisDiscountEventJpaRepository;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;
import kr.hoppang.domain.chassis.event.DiscountType;
import kr.hoppang.domain.chassis.event.repository.ChassisDiscountEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChassisDiscountEventRepositoryAdapter implements ChassisDiscountEventRepository {

    private final ChassisDiscountEventJpaRepository chassisDiscountEventJpaRepository;

    @Override
    public List<ChassisDiscountEvent> findAllChassisDiscountEventByCompanyAndChassisType(
            final CompanyType companyType,
            final List<ChassisType> chassisTypeList,
            final DiscountType discountType
    ) {

        List<ChassisDiscountEventEntity> chassisDiscountEventEntityList = new ArrayList<>();
        if (discountType.equals(DiscountType.PERCENTAGE)) {
            chassisDiscountEventEntityList = chassisDiscountEventJpaRepository.findByCompanyTypeAndChassisTypeAndIsStillAndDiscountTypeOnDiscount(
                    companyType,
                    chassisTypeList,
                    discountType
            );
        } else {
            List<ChassisDiscountEventEntity> chassisDiscountEventEntity = chassisDiscountEventJpaRepository.findByCompanyTypeAndIsStillAndDiscountTypeOnDiscount(
                    companyType,
                    discountType
            );

            if (chassisDiscountEventEntity != null) {
                chassisDiscountEventEntityList.addAll(chassisDiscountEventEntity);
            }
        }

        if (chassisDiscountEventEntityList.isEmpty()) {
            return List.of();
        }

        return chassisDiscountEventEntityList.stream()
                .map(ChassisDiscountEventEntity::toPojo)
                .toList();
    }

    @Override
    public ChassisDiscountEvent findChassisDiscountEventById(final long id) {

        Optional<ChassisDiscountEventEntity> chassisDiscountEventEntity =
                chassisDiscountEventJpaRepository.findById(id);

        return chassisDiscountEventEntity
                .map(ChassisDiscountEventEntity::toPojo)
                .orElse(null);
    }
}
