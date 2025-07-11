package kr.hoppang.adapter.outbound.jpa.repository.user;

import static com.querydsl.core.types.ExpressionUtils.allOf;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_TOKEN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_USER;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_USER_CONFIGURATION;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.adapter.outbound.jpa.entity.user.QUserDeviceEntity.userDeviceEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.user.QUserEntity.userEntity;
import static kr.hoppang.util.common.BoolType.convertBooleanToType;
import static kr.hoppang.util.converter.user.UserEntityConverter.userLoginHistoryToEntity;
import static kr.hoppang.util.converter.user.UserEntityConverter.userToEntity;
import static kr.hoppang.util.converter.user.UserEntityConverter.userTokenToEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserConfigInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserTokenEntity;
import kr.hoppang.adapter.outbound.jpa.repository.user.dto.FindUsersIdInDto;
import kr.hoppang.adapter.outbound.jpa.repository.user.userconfiginfo.UserConfigInfoJpaRepository;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.UserConfigInfo;
import kr.hoppang.domain.user.UserDevice;
import kr.hoppang.domain.user.UserLoginHistory;
import kr.hoppang.domain.user.UserRole;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@Transactional
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JPAQueryFactory queryFactory;
    private final UserJpaRepository userJpaRepository;
    private final UserConfigInfoJpaRepository userConfigInfoJpaRepository;
    private final UserLoginHistoryJpaRepository userLoginHistoryJpaRepository;


    @Override
    @Transactional(readOnly = true)
    public User findById(final long userId) {
        Optional<UserEntity> entity = userJpaRepository.findByIdAndDeletedAtIsNull(userId);

        check(entity.isEmpty(), NOT_EXIST_USER);

        return entity.get().toPojoWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(final String email) {
        UserEntity entity = userJpaRepository.findByEmailAndDeletedAtIsNull(email);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmailWithoutRelations(final String email) {
        UserEntity entity = userJpaRepository.findByEmailAndDeletedAtIsNull(email);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }

    @Override
    @Transactional(readOnly = true)
    public User findIfExistUserByEmail(final String email, final OauthType oauthType) {

        UserEntity user = userJpaRepository.findByEmailAndOauthTypeAndDeletedAtIsNull(email, oauthType);

        return user != null ? user.toPojoWithRelations() : null;
    }

    @Override
    public User save(final User user) {
        UserEntity entity = userJpaRepository.save(userToEntity(user));

        for (UserToken userToken : user.getUserTokenList()) {
            entity.getUserTokenEntityList().add(userTokenToEntity(entity.getId(), userToken));
        }

        if (user.getUserAddress() != null) {
            entity.setUserAddress(user.getUserAddress());
        }

        if (!user.getUserDeviceList().isEmpty()) {
            entity.setUserDeviceInfo(user.getUserDeviceList());
        }

        if (user.getUserConfigInfo() != null) {
            entity.setUserConfigInfo(user.getUserConfigInfo());
        }

        return entity.toPojoWithRelations();
    }

    @Override
    public void updateToken(final String email, final TokenType tokenType, final String token,
            final LocalDateTime expireTime) {

        UserEntity entity = userJpaRepository.findByEmailAndDeletedAtIsNull(email);

        check(entity == null, NOT_EXIST_USER);

        UserTokenEntity userToken = entity.getUserTokenEntityList().stream()
                .filter(f -> tokenType.equals(f.getTokenType()))
                .findFirst()
                .orElseThrow(() -> new HoppangLoginException(NOT_EXIST_TOKEN));

        if (token == null) {
            userToken.reviseToken(expireTime);
            return;
        }

        userToken.reviseToken(token, expireTime);
    }

    @Override
    public User userConfiguration(
            final String userEmail,
            final String phoneNumber,
            final boolean isPushOn,
            final boolean isAlimTalkOn) {

        UserEntity entity = userJpaRepository.findByEmailAndDeletedAtIsNull(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.updatePhoneNumberAndAddressAndConfig(phoneNumber, isPushOn, isAlimTalkOn);

        return entity.toPojoWithRelations();
    }

    @Override
    public User updateDeviceInfo(final String userEmail, final UserDevice userDevice) {

        UserEntity entity = userJpaRepository.findByEmailAndDeletedAtIsNull(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.setUserDeviceInfo(userDevice);

        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateRequiredReLogin(final String userEmail) {

        UserEntity entity = userJpaRepository.findByEmailAndDeletedAtIsNull(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.updateToBeRequiredReLogin();
    }

    @Override
    public User updateUserTokenInfo(final String userEmail, final List<UserToken> userTokens) {

        UserEntity entity = userJpaRepository.findByEmailAndDeletedAtIsNull(userEmail);

        check(entity == null, NOT_EXIST_USER);

        for (UserToken userToken : userTokens) {
            entity.getUserTokenEntityList().stream()
                    .filter(f -> f.getTokenType().equals(userToken.getTokenType()))
                    .forEach(e -> e.updateTokenAndExpireTime(
                            userToken.getToken(),
                            userToken.getExpireIn()));
        }

        entity.updateNotToBeRequiredReLogin();

        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteUser(final String email) {
        userJpaRepository.deleteByEmail(email);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String softDeleteUser(final long id) {
        Optional<UserEntity> entity = userJpaRepository.findById(id);

        UserEntity user = entity.orElse(null);

        check(user == null, NOT_EXIST_USER);

        userJpaRepository.deleteAllTokensOfTheUser(id); // 모든 토큰 정보 삭제
        userJpaRepository.deleteUserSoftly(id, LocalDateTime.now()); // 유저 삭제 로직 수행

        return user.getEmail();
    }

    @Override
    public void updateUserConfiguration(
            final long id,
            final boolean isPushOn,
            final boolean isAlimTalkOn
    ) {
        Optional<UserEntity> entity = userJpaRepository.findById(id);

        UserEntity user = entity.orElse(null);

        check(user == null, NOT_EXIST_USER);

        if (user.getUserConfigInfo() == null) {
            user.setUserConfigInfo(
                    UserConfigInfoEntity.of(
                            user.getId(),
                            convertBooleanToType(isPushOn),
                            convertBooleanToType(isAlimTalkOn)
                    ));
        }

        user.updateIsPushOn(isPushOn);
        user.updateIsAlimTalkOn(isAlimTalkOn);
    }

    @Override
    @Transactional(readOnly = true)
    public UserConfigInfo findUserConfigByUserId(final long userId) {

        UserConfigInfoEntity userConfigInfo = userConfigInfoJpaRepository.findByUserId(userId);

        check(userConfigInfo == null, NOT_EXIST_USER_CONFIGURATION);

        return userConfigInfo.toPojo();
    }

    @Override
    public void createUserLoginHistory(final UserLoginHistory userLoginHistory) {

        userLoginHistoryJpaRepository.save(userLoginHistoryToEntity(userLoginHistory));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllAvailableUsers(final long offset, final int limit) {

        List<UserEntity> userEntityList = queryFactory
                .select(userEntity)
                .from(userEntity)
                .where(userEntity.deletedAt.isNull())
                .offset(offset)
                .limit(limit)
                .fetch();

        if (userEntityList.isEmpty()) {
            return List.of();
        }

        return userEntityList.stream()
                .map(UserEntity::toPojo)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long findCountOfAllAvailableUsers() {

        Long count = queryFactory.select(userEntity.count())
                .from(userEntity)
                .where(userEntity.deletedAt.isNull())
                .fetchOne();

        return count != null ? count : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long findCountOfAllUsers() {

        int count = queryFactory
                .selectDistinct(userEntity.email, userEntity.tel)
                .from(userEntity)
                .where(
                        userEntity.role.eq(UserRole.ROLE_CUSTOMER)
                                .and(userEntity.tel.notIn(
                                        "01088257754", "01029143611"
                                ))
                )
                .fetch().size();

        return (long) count;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllRegisteredUsersBetween(final LocalDateTime start,
            final LocalDateTime end) {

        List<UserEntity> userEntityList = queryFactory.select(userEntity)
                .from(userEntity)
                .where(
                        allOf(
                                userEntity.role.eq(UserRole.ROLE_CUSTOMER),
                                userEntity.tel.notIn(
                                        "01088257754", "01029143611"
                                ),
                                userEntity.createdAt.between(start, end)
                        )
                ).fetch();

        return userEntityList.stream()
                .map(UserEntity::toPojo)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllDeletedUsersBetween(final LocalDateTime start,
            final LocalDateTime end) {

        List<UserEntity> userEntityList = queryFactory.select(userEntity)
                .from(userEntity)
                .where(
                        allOf(
                                userEntity.role.eq(UserRole.ROLE_CUSTOMER),
                                userEntity.tel.notIn(
                                        "01088257754", "01029143611"
                                ),
                                userEntity.deletedAt.between(start, end)
                        )
                ).fetch();

        return userEntityList.stream()
                .map(UserEntity::toPojo)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsersByDeviceType(final String deviceType) {

        List<UserEntity> userEntityList = queryFactory
                .selectFrom(userEntity)
                .innerJoin(userDeviceEntity).on(userEntity.id.eq(userDeviceEntity.id)).fetchJoin()
                .where(
                        allOf(
                                userEntity.role.eq(UserRole.ROLE_CUSTOMER),
                                userEntity.tel.notIn(
                                        "01088257754", "01029143611"
                                ),
                                userDeviceEntity.deviceType.eq(deviceType)
                        )
                )
                .fetch();

        if (userEntityList.isEmpty()) {
            return List.of();
        }

        return userEntityList.stream()
                .map(UserEntity::toPojo)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsersIdIn(final List<Long> userIds) {
        List<FindUsersIdInDto> usersResult = queryFactory
                .select(
                        Projections.constructor(FindUsersIdInDto.class,
                        userEntity.id,
                        userEntity.name,
                        userEntity.deletedAt
                ))
                .from(userEntity)
                .where(
                        allOf(
                                userEntity.id.in(userIds)
                        )
                )
                .fetch();

        return usersResult.stream()
                .map(user ->
                        User.builder()
                                .id(user.id())
                                .name(user.name())
                                .deletedAt(user.deletedAt())
                                .build()
                ).toList();
    }
}
