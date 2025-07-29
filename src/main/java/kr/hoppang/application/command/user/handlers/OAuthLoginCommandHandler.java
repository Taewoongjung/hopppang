package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commandresults.OAuthLoginCommandResult;
import kr.hoppang.application.command.user.commands.AddUserLoginHistoryCommand;
import kr.hoppang.application.command.user.commands.OAuthLoginCommand;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.oauth.OAuthServiceAdapter;
import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.config.security.jwt.JWTUtil;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginCommandHandler implements ICommandHandler<OAuthLoginCommand, OAuthLoginCommandResult> {

    private final JWTUtil jwtUtil;
    private final OAuthServiceAdapter oAuthServiceAdapter;
    private final ApplicationEventPublisher eventPublisher;
    private final SignUpCommandHandler signUpCommandHandler;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public OAuthLoginCommandResult handle(final OAuthLoginCommand command) throws Exception {

        log.info("[핸들러 - 소셜 ({}) 로그인] OAuthLoginCommand = {}", command.oauthType().getType(),
                command);

        // 클라이언트로 부터 받은 code 값으로 유저 정보 파싱
        OAuthLoginResultDto oAuthLoginResult = oAuthServiceAdapter.logIn(command.oauthType(),
                command.tokenInfo());

        log.info("구글로 부터 온 데이터 = {}", oAuthLoginResult);

        User registeredUser = signUpCommandHandler.handle(
                new SignUpCommand(
                        oAuthLoginResult.name(),
                        oAuthLoginResult.password(),
                        oAuthLoginResult.email(),
                        oAuthLoginResult.tel(),
                        oAuthLoginResult.role(),
                        oAuthLoginResult.oauthType(),
                        command.deviceId(),
                        command.deviceType(),
                        oAuthLoginResult.providerUserId(),
                        oAuthLoginResult.connectedAt(),
                        oAuthLoginResult.accessToken(),
                        oAuthLoginResult.accessTokenExpireIn(),
                        oAuthLoginResult.refreshToken(),
                        oAuthLoginResult.refreshTokenExpireIn()
                ));

        log.info("[핸들러 - 소셜 ({}) 로그인] 성공", command.oauthType().getType());

        eventPublisher.publishEvent(
                AddUserLoginHistoryCommand.builder()
                        .userId(registeredUser.getId())
                        .build()
        );

        return new OAuthLoginCommandResult(
                registeredUser.isFirstLogin(),
                jwtUtil.createJwtForSso( // jwt 토큰 생성
                        registeredUser.getEmail(),
                        registeredUser.getUserRole().name(),
                        registeredUser.getOauthType().name(),
                        convertLocalDateTimeToDate(registeredUser.getExpireInOfAccessToken())),
                registeredUser.getEmail());
    }
}
