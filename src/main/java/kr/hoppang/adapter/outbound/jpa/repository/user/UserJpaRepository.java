package kr.hoppang.adapter.outbound.jpa.repository.user;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.domain.user.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(final String email);

    UserEntity findByEmailAndOauthType(final String email, final OauthType oauthType);

    void deleteByEmail(final String email);

    @Modifying
    @Query("UPDATE UserEntity UE "
            + "SET UE.deletedAt = :now "
            + "WHERE UE.id = :userId ")
    void deleteUserSoftly(final long userId, @Param("now") final LocalDateTime now);

    @Modifying
    @Query("DELETE FROM UserTokenEntity UTE "
            + "WHERE UTE.userId = :userId ")
    void deleteAllTokensOfTheUser(final long userId);

    List<UserEntity> findAllByDeletedAtIsNull();
}
