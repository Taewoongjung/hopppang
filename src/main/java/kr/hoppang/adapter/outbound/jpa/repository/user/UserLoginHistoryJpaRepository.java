package kr.hoppang.adapter.outbound.jpa.repository.user;

import kr.hoppang.adapter.outbound.jpa.entity.user.UserLoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginHistoryJpaRepository extends JpaRepository<UserLoginHistoryEntity, Long> {

}
