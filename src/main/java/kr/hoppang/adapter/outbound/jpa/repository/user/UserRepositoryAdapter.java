package kr.hoppang.adapter.outbound.jpa.repository.user;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_SIGNUP_REQUEST_DUPLICATE_EMAIL;
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
    @Transactional(readOnly = true)
    public User findByEmail(final String email) {
        UserEntity entity = userJpaRepository.findByEmail(email);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }

    @Override
    @Transactional(readOnly = true)
    public void checkIfExistUserByEmail(final String email, final OauthType oauthType) {

        check(userJpaRepository.existsByEmailAndOauthType(email, oauthType),
                INVALID_SIGNUP_REQUEST_DUPLICATE_EMAIL);
    }

    @Override
    @Transactional
    public User save(final User user) {
        UserEntity entity = userJpaRepository.save(userToEntity(user));

        for (UserToken userToken : user.getUserTokenList()) {
            entity.getUserTokenEntityList().add(userTokenToEntity(entity.getId(), userToken));
        }

        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(final Long id) {
        UserEntity entity = userJpaRepository.findById(id).orElse(null);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByPhoneNumber(final String phoneNumber) {
        UserEntity entity = userJpaRepository.findByTel(phoneNumber);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
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
}
