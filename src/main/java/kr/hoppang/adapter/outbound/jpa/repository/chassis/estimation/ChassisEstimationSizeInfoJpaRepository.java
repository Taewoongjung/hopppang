package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation;

import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationSizeInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChassisEstimationSizeInfoJpaRepository extends
        JpaRepository<ChassisEstimationSizeInfoEntity, Long> {

}
