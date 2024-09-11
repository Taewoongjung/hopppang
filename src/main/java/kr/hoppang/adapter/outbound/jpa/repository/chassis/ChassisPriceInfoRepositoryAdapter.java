package kr.hoppang.adapter.outbound.jpa.repository.chassis;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_CHASSIS_PRICE_INFO;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.ChassisPriceInfoEntity;
import kr.hoppang.domain.chassis.ChassisPriceInfo;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.repository.ChassisPriceInfoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@RequiredArgsConstructor
public class ChassisPriceInfoRepositoryAdapter implements ChassisPriceInfoRepository {

    private final ChassisPriceInfoJpaRepository chassisPriceInfoJpaRepository;


    @Override
    @Transactional(readOnly = true)
    public List<ChassisPriceInfo> findByTypeAndCompanyType(
            final ChassisType type,
            final CompanyType companyType) {

        List<ChassisPriceInfoEntity> chassisPriceInfoEntityList =
                chassisPriceInfoJpaRepository.findAllByTypeAndCompanyType(type, companyType);

        check(chassisPriceInfoEntityList.isEmpty(), NOT_EXIST_CHASSIS_PRICE_INFO);

        return chassisPriceInfoEntityList.stream().map(ChassisPriceInfoEntity::toPojo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ChassisPriceInfo findByTypeAndCompanyTypeAndWidthAndHeight(
            final ChassisType type,
            final CompanyType companyType,
            final int width,
            final int height
    ) {
        ChassisPriceInfoEntity chassisPriceInfoEntity =
                chassisPriceInfoJpaRepository.findAllByTypeAndCompanyTypeAndWidthAndHeight(
                        type, companyType, width, height);

        check(chassisPriceInfoEntity == null, NOT_EXIST_CHASSIS_PRICE_INFO);

        return chassisPriceInfoEntity.toPojo();
    }
}
