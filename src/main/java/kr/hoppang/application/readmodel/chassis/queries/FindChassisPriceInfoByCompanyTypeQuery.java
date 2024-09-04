package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public record FindChassisPriceInfoByCompanyTypeQuery(
        ChassisType chassisType,
        CompanyType companyType

) implements IQuery {

}
