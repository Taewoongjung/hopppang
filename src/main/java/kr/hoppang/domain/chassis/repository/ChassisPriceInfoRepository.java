package kr.hoppang.domain.chassis.repository;

import java.util.List;
import kr.hoppang.domain.chassis.price.ChassisPriceInfo;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public interface ChassisPriceInfoRepository {

    List<ChassisPriceInfo> findByTypeAndCompanyType(final ChassisType type, final CompanyType companyType);

    ChassisPriceInfo findByTypeAndCompanyTypeAndWidthAndHeight(
            final ChassisType type, final CompanyType companyType, final int width, final int height);
}
