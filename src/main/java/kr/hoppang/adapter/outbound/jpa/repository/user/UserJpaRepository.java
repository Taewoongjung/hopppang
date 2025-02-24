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

    UserEntity findByEmail(String email);

    UserEntity findByEmailAndOauthType(String email, OauthType oauthType);

    void deleteByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity UE "
            + "SET UE.deletedAt = :now "
            + "WHERE UE.id = :userId ")
    void deleteUserSoftly(long userId, @Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM UserTokenEntity UTE "
            + "WHERE UTE.userId = :userId ")
    void deleteAllTokensOfTheUser(long userId);

    List<UserEntity> findAllByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
