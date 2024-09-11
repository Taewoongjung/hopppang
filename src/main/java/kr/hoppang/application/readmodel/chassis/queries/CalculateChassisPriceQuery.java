package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public record CalculateChassisPriceQuery(
        ChassisType chassisType,
        CompanyType companyType,
        int width,
        int height,
        int floorCustomerLiving,
        boolean isScheduledForDemolition,
        boolean isResident
        ) implements IQuery {

}
