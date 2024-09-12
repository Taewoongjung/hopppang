package kr.hoppang.adapter.outbound.jpa.repository.chassis.pricecriteria;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_ADDITIONAL_CRITERIA_PRICE_INFO;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.domain.chassis.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
}
