package kr.hoppang.application.readmodel.chassis.handlers;

import java.util.List;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceAdditionalCriteriaQuery;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.price.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindChassisPriceAdditionalCriteriaQueryHandler implements
        IQueryHandler<FindChassisPriceAdditionalCriteriaQuery, List<AdditionalChassisPriceCriteria>> {

    private final AdditionalChassisPriceCriteriaRepository additionalChassisPriceCriteriaRepository;

    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Cacheable(
            value = "additionalChassisPriceCriteria",
            cacheManager = "cacheManager"
    )
    public List<AdditionalChassisPriceCriteria> handle(final FindChassisPriceAdditionalCriteriaQuery event) {
        log.info("executed FindChassisPriceAdditionalCriteriaQuery = {}", event);
        
        return additionalChassisPriceCriteriaRepository.findAll();
    }
}
