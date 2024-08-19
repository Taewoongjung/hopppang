package kr.hoppang.adapter.outbound.jpa.entity.user;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_USER_INFO;
import static kr.hoppang.adapter.common.util.CheckUtil.require;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@ToString
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Length(min = 10, max = 11)
    @Column(name = "tel", nullable = false)
    private String tel;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar")
    private UserRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "oauth_type", nullable = false, columnDefinition = "char(3)")
    private OauthType oauthType;

    private String token;

    private String deviceId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserAddressEntity userAddress;

    private UserEntity(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole role,
            final OauthType oauthType,
            final String token,
            final String deviceId
    ) {
        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.role = role;
        this.oauthType = oauthType;
        this.token = token;
        this.deviceId = deviceId;
    }

    public static UserEntity of(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType,
            final String token,
            final String deviceId
    ) {

        return new UserEntity(id, name, email, password, tel, userRole, oauthType, token, deviceId);
    }

    public static UserEntity of(
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType,
            final String token,
            final String deviceId
    ) {

        require(o -> name == null, name, INVALID_USER_INFO);

        return new UserEntity(null, name, email, password, tel, userRole, oauthType, token, deviceId);
    }

    // 모든 연관 관계 제외 한 POJO 객체 리턴
    public User toPojo() {
        return User.of(
                getId(),
                getName(),
                getEmail(),
                getPassword(),
                getTel(),
                getRole(),
                getOauthType(),
                getToken(),
                getDeviceId(),
                getLastModified(),
                getCreatedAt()
        );
    }

    // 모든 연관 관계와 매핑 된 POJO 객체 리턴
    public User toPojoWithRelations() {
        return User.of(
                getId(),
                getName(),
                getEmail(),
                getPassword(),
                getTel(),
                getRole(),
                getOauthType(),
                getToken(),
                getDeviceId(),
                getLastModified(),
                getCreatedAt()
        );
    }

    public void resetPassword(final String targetPwd) {
        this.password = targetPwd;
    }
}
