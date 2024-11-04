package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_VERIFIED_PHONE;
import static kr.hoppang.adapter.common.exception.ErrorType.PLEASE_LOGIN_AGAIN;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.adapter.common.util.CheckUtil.expiredRefreshedTokenCheck;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.alarm.dto.NewUser;
import kr.hoppang.adapter.outbound.cache.dto.TearDownBucketByKey;
import kr.hoppang.adapter.outbound.cache.sms.CacheSmsValidationRedisRepository;
import kr.hoppang.adapter.outbound.cache.user.CacheUserRedisRepository;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserDevice;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpCommandHandler implements ICommandHandler<SignUpCommand, User> {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CacheUserRedisRepository cacheUserRedisRepository;
    private final CacheSmsValidationRedisRepository cacheSmsValidationRedisRepository;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User handle(final SignUpCommand event) {
        log.info("[핸들러 - 회원 생성] SignUpCommand = {}", event);

        User isUserExist = userRepository.findIfExistUserByEmail(event.email(), event.oauthType());

        // 이미 회원이 있고, 해당 회원이 다른 디바이스에서 소셜 로그인을 했을 때 (해당 메소드는 소셜 로그인에서 요청 하기 때문에 해당 처리를 함)
        if (isUserExist != null) {

            // 모든 토큰이 만료 되어서 다시 로그인이 필요한 유저 start
            if (BoolType.T.equals(isUserExist.getRequiredReLogin())) {

                // 토큰 데이터 생성 start
                UserToken forAccessToken = UserToken.of(
                        String.valueOf(event.providerUserId()),
                        TokenType.ACCESS,
                        event.accessToken(),
                        event.connectedAt(),
                        event.accessTokenExpireIn());

                UserToken forRefreshToken = UserToken.of(
                        String.valueOf(event.providerUserId()),
                        TokenType.REFRESH,
                        event.refreshToken(),
                        event.connectedAt(),
                        OauthType.APL.equals(event.oauthType()) ?
                                LocalDateTime.now().plusYears(10L) // 애플은 리프레시토큰 유효기간이 없음
                                : event.refreshTokenExpireIn());

                List<UserToken> userTokenList = new ArrayList<>();
                userTokenList.add(forAccessToken);
                userTokenList.add(forRefreshToken);
                // 토큰 데이터 생성 end

                User user = userRepository.updateUserTokenInfo(isUserExist.getEmail(),
                        userTokenList);

                user.setNotTheFirstLogin();

                return user;
            }
            // 모든 토큰이 만료 되어서 다시 로그인이 필요한 유저 end

            // 여기에서 이미 회원이 있을 때 리프레시 토큰이 유효한지 검증.
            // 아니면 에러 던짐. (에러에 /api/{kakao || apple}/auth 응답값을. 그래서 유저가 해당 uri를 타고 가서 로그인할 수 있게 유도 함.)
            checkIfLoggedInUserWithExpiredRefreshToken(isUserExist);

            User updatedUser = userRepository.updateDeviceInfo(event.email(),
                    UserDevice.of(event.deviceType(), event.deviceId()));

            updatedUser.setNotTheFirstLogin();

            return updatedUser;
        }

        // 토큰 데이터 생성 start
        UserToken forAccessToken = UserToken.of(
                String.valueOf(event.providerUserId()),
                TokenType.ACCESS,
                event.accessToken(),
                event.connectedAt(),
                event.accessTokenExpireIn());

        UserToken forRefreshToken = UserToken.of(
                String.valueOf(event.providerUserId()),
                TokenType.REFRESH,
                event.refreshToken(),
                event.connectedAt(),
                event.refreshTokenExpireIn());

        List<UserToken> userTokenList = new ArrayList<>();
        userTokenList.add(forAccessToken);
        userTokenList.add(forRefreshToken);
        // 토큰 데이터 생성 end

        // 유저 디바이스 정보 생성 start
        List<UserDevice> userDeviceList = new ArrayList<>();
        userDeviceList.add(UserDevice.of(event.deviceType(), event.deviceId()));
        // 유저 디바이스 정보 생성 end

        User user = User.of(
                event.name(),
                event.email(),
                OauthType.NON.equals(event.oauthType()) ?
                        bCryptPasswordEncoder.encode(event.password()) : null,
                event.tel(),
                event.role(),
                event.oauthType(),
                BoolType.F,
                null,
                userTokenList,
                userDeviceList,
                null, null,
                LocalDateTime.now(), LocalDateTime.now());

        User registeredUser = userRepository.save(user);

        // 소셜 로그인 최초에는 유저 전화번호가 빈값일 것이기 때문이다. (전화번호가 있는건 이메일 로그인(NON)이다.)
        if ("".equals(event.tel())) {
            return registeredUser;
        }

        if (registeredUser != null) {

            // 휴대폰 검증이 되었는지 확인
            check(!cacheSmsValidationRedisRepository.getIsVerifiedByKey(event.tel()),
                    NOT_VERIFIED_PHONE);

            // 검증 후 해당 버킷 삭제
            eventPublisher.publishEvent(new TearDownBucketByKey(event.tel()));

            // 새로운 유저 회원가입 시 알람 발송
            eventPublisher.publishEvent(
                    new NewUser(event.name(), event.email(), event.tel(), event.oauthType(),
                            registeredUser.getCreatedAt()));

            // 새로운 유저 캐시 데이터에 추가
            cacheUserRedisRepository.addUserInfoInCache(registeredUser.getEmail(), registeredUser);

            log.info("[핸들러 - 회원 생성] 성공");

            return registeredUser;
        }

        return null;
    }

    private void checkIfLoggedInUserWithExpiredRefreshToken(final User isUserExist) {
        UserToken userToken = isUserExist.getTheLatestRefreshToken();

        boolean isRefreshTokenExpired = userToken.getExpireIn().isBefore(LocalDateTime.now());

        if (isRefreshTokenExpired) {
            userRepository.updateRequiredReLogin(isUserExist.getEmail());
        }

        expiredRefreshedTokenCheck(userToken.getExpireIn().isBefore(LocalDateTime.now()), PLEASE_LOGIN_AGAIN);
    }
}
