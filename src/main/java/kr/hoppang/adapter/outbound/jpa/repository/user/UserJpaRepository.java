package kr.hoppang.adapter.outbound.jpa.repository.user;

import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.domain.user.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(final String email);

    UserEntity findByEmailAndOauthType(final String email, final OauthType oauthType);

    Boolean existsByEmailAndOauthType(final String email, final OauthType oauthType);

    @Query("SELECT UE FROM UserEntity UE "
            + "JOIN UserDeviceEntity UDE ON UDE.userId = UE.id "
            + "WHERE UDE.deviceId = :deviceId ")
    UserEntity findByDeviceId(@Param("deviceId") final String deviceId);

    UserEntity findByTel(final String phoneNumber);
}
