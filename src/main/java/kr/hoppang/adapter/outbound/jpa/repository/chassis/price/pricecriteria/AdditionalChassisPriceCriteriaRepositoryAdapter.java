package kr.hoppang.adapter.outbound.jpa.repository.chassis.price.pricecriteria;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_ADDITIONAL_CRITERIA_PRICE_INFO;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.price.pricecriteria.AdditionalChassisPriceCriteriaEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.price.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.price.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@RequiredArgsConstructor
public class AdditionalChassisPriceCriteriaRepositoryAdapter implements
        AdditionalChassisPriceCriteriaRepository {

    private final AdditionalChassisPriceCriteriaJpaRepository additionalChassisPriceCriteriaJpaRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "additionalChassisPriceCriteria", key = "#type")
    public AdditionalChassisPriceCriteria findByType(final AdditionalChassisPriceCriteriaType type) {

        AdditionalChassisPriceCriteriaEntity entity =
                additionalChassisPriceCriteriaJpaRepository.findByType(type);

        check(entity == null, NOT_EXIST_ADDITIONAL_CRITERIA_PRICE_INFO);

        return entity.toPojo();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdditionalChassisPriceCriteria> findAll() {

        List<AdditionalChassisPriceCriteriaEntity> entityList =
                additionalChassisPriceCriteriaJpaRepository.findAll();

        if (entityList.isEmpty()) {
            return new ArrayList<>();
        }

        return entityList.stream()
                .map(AdditionalChassisPriceCriteriaEntity::toPojo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean reviseAdditionalChassisPriceCriteria(
            final List<AdditionalChassisPriceCriteria> targetList) {

        List<AdditionalChassisPriceCriteriaEntity> entityList =
                additionalChassisPriceCriteriaJpaRepository.findAll();

        targetList.forEach(e -> {
            AdditionalChassisPriceCriteriaEntity entity = entityList.stream()
                    .filter(f -> e.getType().equals(f.getType()))
                    .findFirst()
                    .orElseThrow();

            entity.updateLastModified();
            entity.revisePrice(e.getPrice());
        });

        return true;
    }
}
