package kr.hoppang.adapter.outbound.jpa.repository.user.userconfiginfo;

import kr.hoppang.adapter.outbound.jpa.entity.user.UserConfigInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigInfoJpaRepository extends JpaRepository<UserConfigInfoEntity, Long> {

    UserConfigInfoEntity findByUserId(final long userId);
}
