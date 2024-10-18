package kr.hoppang.adapter.outbound.jpa.repository.user;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_TOKEN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_USER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.converter.user.UserEntityConverter.userToEntity;
import static kr.hoppang.util.converter.user.UserEntityConverter.userTokenToEntity;

import java.time.LocalDateTime;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserTokenEntity;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.UserDevice;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByEmail(final String email) {
        UserEntity entity = userJpaRepository.findByEmail(email);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }

    @Override
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

        return entity.toPojoWithRelations();
    }

    @Override
    public User findIfExistUserByPhoneNumber(final String tel) {

        UserEntity entity = userJpaRepository.findByTel(tel);

        return entity != null ? entity.toPojo() : null;
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

        entity.updatePhoneNumberAndAddress(phoneNumber, userAddress);

        return entity.toPojo();
    }

    @Override
    @Transactional
    public User updateDeviceInfo(final String userEmail, final UserDevice userDevice) {

        UserEntity entity = userJpaRepository.findByEmail(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.setUserDeviceInfo(userDevice);

        return entity.toPojo();
    }

    @Override
    @Transactional
    public boolean updateRequiredReLogin(final String userEmail) {

        UserEntity entity = userJpaRepository.findByEmail(userEmail);

        check(entity == null, NOT_EXIST_USER);

        entity.updateToBeRequiredReLogin();

        return true;
    }
}
