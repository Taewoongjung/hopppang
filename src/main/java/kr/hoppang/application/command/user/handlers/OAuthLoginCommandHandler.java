package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commandresults.OAuthLoginCommandResult;
import kr.hoppang.application.command.user.commands.OAuthLoginCommand;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.oauth.OAuthService;
import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.config.security.jwt.JWTUtil;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginCommandHandler implements ICommandHandler<OAuthLoginCommand, OAuthLoginCommandResult> {

    private final JWTUtil jwtUtil;
    private final List<OAuthService> oAuthServices;
    private final SignUpCommandHandler signUpCommandHandler;

    private EnumMap<OauthType, OAuthService> oAuthServiceEnumMap;

    @PostConstruct
    void init() {
        oAuthServiceEnumMap = oAuthServices.stream()
                .collect(Collectors.toMap(
                        OAuthService::getOauthType,
                        (oAuthService) -> oAuthService,
                        (existService, newService) -> existService,
                        () -> new EnumMap<>(OauthType.class)
                ));
    }


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public OAuthLoginCommandResult handle(final OAuthLoginCommand command) {

        log.info("[핸들러 - 소셜 ({}) 로그인] OAuthLoginCommand = {}", command.oauthType().getType(),
                command);

        // 클라이언트로 부터 받은 code 값으로 유저 정보 파싱
        OAuthLoginResultDto oAuthLoginResult = oAuthServiceEnumMap.get(command.oauthType())
                .logIn(command.code());

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

        return new OAuthLoginCommandResult(
                registeredUser.isFirstLogin(),
                jwtUtil.createJwtForSso( // jwt 토큰 생성
                        registeredUser.getEmail(),
                        registeredUser.getUserRole().name(),
                        registeredUser.getOauthType().name(),
                        convertLocalDateTimeToDate(oAuthLoginResult.accessTokenExpireIn())),
                registeredUser.getEmail());
    }
}
