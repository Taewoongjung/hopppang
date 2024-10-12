package kr.hoppang.application.command.user.handlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.alarm.dto.NewUser;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
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

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User handle(final SignUpCommand event) {
        log.info("[핸들러 - 회원 생성] SignUpCommand = {}", event);

        userRepository.checkIfExistUserByEmail(event.email(), event.oauthType());

        // 토큰 데이터 생성
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

        User user = User.of(
                event.name(),
                event.email(),
                OauthType.NON.equals(event.oauthType()) ?
                        bCryptPasswordEncoder.encode(event.password()) : null,
                event.tel(),
                event.role(),
                event.oauthType(),
                event.deviceId(),
                userTokenList,
                LocalDateTime.now(), LocalDateTime.now());

        User registeredUser = userRepository.save(user);

        if (registeredUser != null) {

//            check(!cacheSmsValidationRedisRepository.getIsVerifiedByKey(event.tel()),
//                    NOT_VERIFIED_PHONE);

            // 새로운 유저 회원가입 시 알람 발송
            eventPublisher.publishEvent(
                    new NewUser(event.name(), event.email(), event.tel(), event.oauthType(),
                            registeredUser.getCreatedAt()));

            // 새로운 유저 회원가입 시 해당 유저에게 알림톡 발송
//            eventPublisher.publishEvent(
//                    new SendAlimTalk(null, null, KakaoMsgTemplateType.COMPLETE_SIGNUP, event.tel(),
//                            event.name(), null, null, null));

            log.info("[핸들러 - 회원 생성] 성공");

            return registeredUser;
        }

        return null;
    }
}
