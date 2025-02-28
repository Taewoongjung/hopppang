package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation;

import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChassisEstimationInquiryJpaRepository extends
        JpaRepository<ChassisEstimationInquiryEntity, Long> {

}
