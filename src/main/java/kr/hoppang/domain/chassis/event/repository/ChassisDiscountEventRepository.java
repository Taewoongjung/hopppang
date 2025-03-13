package kr.hoppang.domain.chassis.event.repository;

import java.util.List;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;

public interface ChassisDiscountEventRepository {

    List<ChassisDiscountEvent> findAllChassisDiscountEventByCompanyAndChassisType(
            CompanyType companyType,
            List<ChassisType> chassisTypeList
    );
}
