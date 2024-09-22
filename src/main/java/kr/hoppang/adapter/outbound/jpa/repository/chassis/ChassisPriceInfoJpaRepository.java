package kr.hoppang.adapter.outbound.jpa.repository.chassis;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.price.ChassisPriceInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.price.ChassisPriceInfoId;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChassisPriceInfoJpaRepository extends JpaRepository<ChassisPriceInfoEntity, ChassisPriceInfoId> {

    List<ChassisPriceInfoEntity> findAllByTypeAndCompanyType(final ChassisType type, final CompanyType companyType);

    ChassisPriceInfoEntity findAllByTypeAndCompanyTypeAndWidthAndHeight(final ChassisType type,
            final CompanyType companyType,
            final int width,
            final int height);
}
