package kr.hoppang.domain.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.UserConfigInfo;
import kr.hoppang.domain.user.UserDevice;
import kr.hoppang.domain.user.UserLoginHistory;
import kr.hoppang.domain.user.UserToken;

public interface UserRepository {

    User findById(final long userId);

    User findByEmail(final String email);

    User findByEmailWithoutRelations(final String email);

    User findIfExistUserByEmail(final String email, final OauthType oauthType);

    User save(final User user);

    void updateToken(final String email, final TokenType tokenType, final String token,
            final LocalDateTime expireTime);

    User updatePhoneNumberAndAddressAndPush(String userEmail, String phoneNumber,
            UserAddress userAddress, boolean isPushOn);

    User updateDeviceInfo(final String userEmail, final UserDevice userDevice);

    void updateRequiredReLogin(final String userEmail);

    User updateUserTokenInfo(final String userEmail, final List<UserToken> userTokens);

    void deleteUser(final String email);

    String softDeleteUser(final long id);

    void updateUserConfiguration(final long id, final boolean isPushOn);

    UserConfigInfo findUserConfigByUserId(final long userId);

    void createUserLoginHistory(final UserLoginHistory userLoginHistory);

    List<User> findAllAvailableUsers(final long offset, final int limit);

    Long findCountOfAllAvailableUsers();

    List<User> findAllRegisteredUsersBetween(final LocalDateTime start, final LocalDateTime end);

    List<User> findAllDeletedUsersBetween(final LocalDateTime start, final LocalDateTime end);
}
