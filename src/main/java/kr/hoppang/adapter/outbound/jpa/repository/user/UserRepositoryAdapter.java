package kr.hoppang.adapter.outbound.jpa.repository.user;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_TOKEN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_USER;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_USER_CONFIGURATION;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.common.BoolType.convertBooleanToType;
import static kr.hoppang.util.converter.user.UserEntityConverter.userToEntity;
import static kr.hoppang.util.converter.user.UserEntityConverter.userTokenToEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserConfigInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserTokenEntity;
import kr.hoppang.adapter.outbound.jpa.repository.user.userconfiginfo.UserConfigInfoJpaRepository;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.UserConfigInfo;
import kr.hoppang.domain.user.UserDevice;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserConfigInfoJpaRepository userConfigInfoJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public User findById(final long userId) {
        Optional<UserEntity> entity = userJpaRepository.findById(userId);

        check(entity.isEmpty(), NOT_EXIST_USER);

        return entity.get().toPojoWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(final String email) {
        UserEntity entity = userJpaRepository.findByEmail(email);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmailWithoutRelations(final String email) {
        UserEntity entity = userJpaRepository.findByEmail(email);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }

    @Override
    @Transactional(readOnly = true)
    public User findIfExistUserByEmail(final String email, final OauthType oauthType) {

        UserEntity user = userJpaRepository.findByEmailAndOauthType(email, oauthType);

        return user != null ? user.toPojoWithRelations() : null;
    }

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public User findIfExistUserByPhoneNumber(final String tel) {

        UserEntity entity = userJpaRepository.findByTel(tel);

        return entity != null ? entity.toPojoWithRelations() : null;
    }

    @Override
    @Transactional
    public void updateToken(final String email, final TokenType tokenType, final String token,
            final LocalDateTime expireTime) {

        UserEntity entity = userJpaRepository.findByEmail(email);

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
    @Transactional
    public User updatePhoneNumberAndAddressAndPush(
            final String userEmail,
            final String phoneNumber,
            final UserAddress userAddress,
            final boolean isPushOn) {

        UserEntity entity = userJpaRepository.findByEmail(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.updatePhoneNumberAndAddressAndConfig(phoneNumber, userAddress, isPushOn);

        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional
    public User updateDeviceInfo(final String userEmail, final UserDevice userDevice) {

        UserEntity entity = userJpaRepository.findByEmail(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.setUserDeviceInfo(userDevice);

        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateRequiredReLogin(final String userEmail) {

        UserEntity entity = userJpaRepository.findByEmail(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.updateToBeRequiredReLogin();
    }

    @Override
    @Transactional
    public User updateUserTokenInfo(final String userEmail, final List<UserToken> userTokens) {

        UserEntity entity = userJpaRepository.findByEmail(userEmail);

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
    @Transactional
    public void updateUserConfiguration(final long id, final boolean isPushOn) {
        Optional<UserEntity> entity = userJpaRepository.findById(id);

        UserEntity user = entity.orElse(null);

        check(user == null, NOT_EXIST_USER);

        if (user.getUserConfigInfo() == null) {
            user.setUserConfigInfo(
                    UserConfigInfoEntity.of(user.getId(), convertBooleanToType(isPushOn)));
        }

        user.updateIsPushOn(isPushOn);
    }

    @Override
    @Transactional(readOnly = true)
    public UserConfigInfo findUserConfigByUserId(final long userId) {

        UserConfigInfoEntity userConfigInfo = userConfigInfoJpaRepository.findByUserId(userId);

        check(userConfigInfo == null, NOT_EXIST_USER_CONFIGURATION);

        return userConfigInfo.toPojo();
    }
}
