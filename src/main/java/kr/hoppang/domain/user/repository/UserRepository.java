package kr.hoppang.domain.user.repository;

import java.time.LocalDateTime;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.UserDevice;

public interface UserRepository {

    User findByEmail(final String email);

    User checkIfAlreadyLoggedIn(final String deviceId);

    User findIfExistUserByEmail(final String email, final OauthType oauthType);

    boolean checkIfExistUserByEmail(final String email, final OauthType oauthType);

    User save(final User user);

    User findById(final Long id);

    User findByPhoneNumber(final String phoneNumber);

    User findIfExistUserByPhoneNumber(final String tel);

    void updateToken(final String email, final TokenType tokenType, final String token,
            final LocalDateTime expireTime);

    User updatePhoneNumberAndAddressAndPush(String userEmail, String phoneNumber,
            UserAddress userAddress, boolean isPushOn);

    User updateDeviceInfo(final String userEmail, final UserDevice userDevice);

}
