package kr.hoppang.domain.chassis.repository.pricecriteria;

import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.domain.chassis.pricecriteria.AdditionalChassisPriceCriteria;

public interface AdditionalChassisPriceCriteriaRepository {

    AdditionalChassisPriceCriteria findByType(final AdditionalChassisPriceCriteriaType type);
}
