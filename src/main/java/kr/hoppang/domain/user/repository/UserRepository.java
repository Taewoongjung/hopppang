package kr.hoppang.domain.user.repository;

import java.time.LocalDateTime;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;

public interface UserRepository {

    User findByEmail(final String email);

    void checkIfExistUserByEmail(final String email, final OauthType oauthType);

    User save(final User user);

    User findById(final Long id);

    User findByPhoneNumber(final String phoneNumber);

    void updateToken(final String email, final TokenType tokenType, final String token,
            final LocalDateTime expireTime);
}
