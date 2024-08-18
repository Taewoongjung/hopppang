package kr.hoppang.adapter.outbound.jpa.repository.user;

import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(final String email);

    Boolean existsByEmail(final String email);

    Boolean existsByTel(final String tel);

    UserEntity findByTel(final String phoneNumber);
}
