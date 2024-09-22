package kr.hoppang.domain.chassis.repository.pricecriteria;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.price.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;

public interface AdditionalChassisPriceCriteriaRepository {

    AdditionalChassisPriceCriteria findByType(final AdditionalChassisPriceCriteriaType type);

    List<AdditionalChassisPriceCriteria> findAll();

    boolean reviseAdditionalChassisPriceCriteria(final List<AdditionalChassisPriceCriteria> targetList);
}
