package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_VERIFIED_PHONE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.alarm.dto.NewUser;
import kr.hoppang.adapter.outbound.cache.common.TearDownBucketByKey;
import kr.hoppang.adapter.outbound.cache.sms.CacheSmsValidationRedisRepository;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserDevice;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
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
    private final CacheSmsValidationRedisRepository cacheSmsValidationRedisRepository;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User handle(final SignUpCommand event) {
        log.info("[핸들러 - 회원 생성] SignUpCommand = {}", event);

        userRepository.checkIfExistUserByEmail(event.email(), event.oauthType());

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
                userTokenList,
                userDeviceList,
                null,
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

            log.info("[핸들러 - 회원 생성] 성공");

            return registeredUser;
        }

        return null;
    }
}
