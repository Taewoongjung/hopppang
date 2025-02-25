package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChassisEstimationInfoJpaRepository extends JpaRepository<ChassisEstimationInfoEntity, Long> {

    List<ChassisEstimationInfoEntity> findAllByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
