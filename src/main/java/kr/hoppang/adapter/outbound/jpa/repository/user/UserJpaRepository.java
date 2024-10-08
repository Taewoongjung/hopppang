package kr.hoppang.adapter.outbound.jpa.repository.user;

import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.domain.user.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(final String email);

    Boolean existsByEmailAndOauthType(final String email, final OauthType oauthType);

    Boolean existsByTel(final String tel);

    UserEntity findByTel(final String phoneNumber);
}
