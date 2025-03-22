package kr.hoppang.adapter.outbound.jpa.repository.chassis.event;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.event.ChassisDiscountEventEntity;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.event.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChassisDiscountEventJpaRepository extends
        JpaRepository<ChassisDiscountEventEntity, Long> {

    @Query(
            "SELECT cde "
                    + "FROM ChassisDiscountEventEntity cde "
                    + "WHERE cde.companyType = :companyType "
                    + "AND cde.chassisType IN :chassisTypeList "
                    + "AND cde.isStillOnDiscount = 'T'"
                    + "AND cde.discountType = :discountType")
    List<ChassisDiscountEventEntity> findByCompanyTypeAndChassisTypeAndIsStillAndDiscountTypeOnDiscount(
            @Param("companyType") CompanyType companyType,
            @Param("chassisTypeList") List<ChassisType> chassisTypeList,
            @Param("discountType")DiscountType discountType
    );

    @Query(
            "SELECT cde "
                    + "FROM ChassisDiscountEventEntity cde "
                    + "WHERE cde.companyType = :companyType "
                    + "AND cde.isStillOnDiscount = 'T'"
                    + "AND cde.discountType = :discountType")
    List<ChassisDiscountEventEntity> findByCompanyTypeAndIsStillAndDiscountTypeOnDiscount(
            @Param("companyType") CompanyType companyType,
            @Param("discountType") DiscountType discountType
    );
}
