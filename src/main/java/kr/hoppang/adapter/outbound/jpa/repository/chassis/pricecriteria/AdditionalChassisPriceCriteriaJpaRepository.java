package kr.hoppang.adapter.outbound.jpa.repository.chassis.pricecriteria;

import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalChassisPriceCriteriaJpaRepository extends JpaRepository<AdditionalChassisPriceCriteriaEntity, AdditionalChassisPriceCriteriaType> {

    AdditionalChassisPriceCriteriaEntity findByType(final AdditionalChassisPriceCriteriaType type);
}
