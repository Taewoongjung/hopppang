package kr.hoppang.application.readmodel.chassis.handlers;

import java.util.List;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceInfoByCompanyTypeQuery;
import kr.hoppang.domain.chassis.price.ChassisPriceInfo;
import kr.hoppang.domain.chassis.repository.ChassisPriceInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler implements IQueryHandler<FindChassisPriceInfoByCompanyTypeQuery, List<ChassisPriceInfo>> {

    private final ChassisPriceInfoRepository chassisPriceInfoRepository;

    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    public List<ChassisPriceInfo> handle(final FindChassisPriceInfoByCompanyTypeQuery event) {

        return chassisPriceInfoRepository.findByTypeAndCompanyType(event.chassisType(), event.companyType());
    }
}
