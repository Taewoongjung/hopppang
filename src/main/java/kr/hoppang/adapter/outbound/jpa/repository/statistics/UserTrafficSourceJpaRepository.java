package kr.hoppang.adapter.outbound.jpa.repository.statistics;

import kr.hoppang.adapter.outbound.jpa.entity.statistics.UserTrafficSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTrafficSourceJpaRepository extends
        JpaRepository<UserTrafficSourceEntity, Long> {

}
