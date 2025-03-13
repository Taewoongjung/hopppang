package kr.hoppang.adapter.outbound.jpa.repository.chassis.event;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.event.ChassisDiscountEventEntity;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChassisDiscountEventJpaRepository extends
        JpaRepository<ChassisDiscountEventEntity, Long> {

    @Query(
            "SELECT cde "
                    + "FROM ChassisDiscountEventEntity cde "
                    + "WHERE cde.companyType = :companyType "
                    + "AND cde.chassisType IN :chassisTypeList "
                    + "AND cde.isStillOnDiscount = 'T'")
    List<ChassisDiscountEventEntity> findByCompanyTypeAndChassisTypeAndIsStillOnDiscount(
            @Param("companyType") CompanyType companyType,
            @Param("chassisTypeList") List<ChassisType> chassisTypeList
    );
}
