package kr.hoppang.adapter.outbound.jpa.entity.user;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_USER_INFO;
import static kr.hoppang.adapter.common.util.CheckUtil.require;
import static kr.hoppang.util.converter.user.UserEntityConverter.userAddressToEntity;
import static kr.hoppang.util.converter.user.UserEntityConverter.userDeviceToEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.UserDevice;
import kr.hoppang.domain.user.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @Column(name = "tel", nullable = false)
    private String tel;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar")
    private UserRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "oauth_type", nullable = false, columnDefinition = "char(3)")
    private OauthType oauthType;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserAddressEntity userAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTokenEntity> userTokenEntityList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDeviceEntity> userDeviceEntityList = new ArrayList<>();

    private UserEntity(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole role,
            final OauthType oauthType,
            final List<UserTokenEntity> userTokenEntityList
    ) {
        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.role = role;
        this.oauthType = oauthType;
        this.userTokenEntityList = userTokenEntityList;
    }

    public static UserEntity of(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType
    ) {

        return new UserEntity(id, name, email, password, tel, userRole, oauthType,
                new ArrayList<>());
    }

    public static UserEntity of(
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType
    ) {

        require(o -> name == null, name, INVALID_USER_INFO);

        return new UserEntity(null, name, email, password, tel, userRole, oauthType,
                new ArrayList<>());
    }

    public static UserEntity of(
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType,
            final List<UserTokenEntity> userTokenEntityList
    ) {

        require(o -> name == null, name, INVALID_USER_INFO);

        return new UserEntity(null, name, email, password, tel, userRole, oauthType,
                userTokenEntityList);
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
                getUserTokenEntityList().stream()
                        .map(UserTokenEntity::toPojo)
                        .collect(Collectors.toList()),
                getUserDeviceEntityList().stream()
                        .map(UserDeviceEntity::toPojo)
                        .collect(Collectors.toList()),
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
                getUserTokenEntityList().stream()
                        .map(UserTokenEntity::toPojo)
                        .collect(Collectors.toList()),
                getUserDeviceEntityList().stream()
                        .map(UserDeviceEntity::toPojo)
                        .collect(Collectors.toList()),
                getLastModified(),
                getCreatedAt()
        );
    }

    public void resetPassword(final String targetPwd) {
        this.password = targetPwd;
    }

    public void setUserAddress(final UserAddress userAddress) {
        this.userAddress = userAddressToEntity(id, userAddress);
    }

    public void updatePhoneNumberAndAddress(final String phoneNumber,
            final UserAddress userAddress) {

        this.tel = phoneNumber;
        this.userAddress = UserAddressEntity.of(
                this.id,
                userAddress.getAddress(),
                userAddress.getSubAddress(),
                userAddress.getBuildingNumber()
        );
    }

    public void setUserDeviceInfo(final List<UserDevice> userDeviceList) {

        List<UserDeviceEntity> userDeviceEntityListToBeSet = new ArrayList<>();

        for (UserDevice userDevice : userDeviceList) {
            userDeviceEntityListToBeSet.add(userDeviceToEntity(this.id, userDevice));
        }

        this.userDeviceEntityList = userDeviceEntityListToBeSet;
    }

    public void setUserDeviceInfo(final UserDevice userDevice) {

        if (getUserDeviceEntityList().stream()
                .noneMatch(f -> userDevice.getDeviceId().equals(f.getDeviceId()))) {

            this.userDeviceEntityList.add(userDeviceToEntity(this.id, userDevice));
        }
    }
}
