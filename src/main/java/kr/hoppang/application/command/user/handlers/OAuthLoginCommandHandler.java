package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commands.OAuthLoginCommand;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.oauth.OAuthService;
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
public class OAuthLoginCommandHandler implements ICommandHandler<OAuthLoginCommand, String> {

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
    public String handle(final OAuthLoginCommand command) {

        SignUpCommand signUpCommand = oAuthServiceEnumMap.get(command.oauthType())
                .logIn(command.code(), command.deviceId());

        User registeredUser = signUpCommandHandler.handle(signUpCommand);

        // jwt 토큰을 반환한다.
        return jwtUtil.createJwtForSso(
                registeredUser.getEmail(),
                registeredUser.getUserRole().name(),
                registeredUser.getOauthType().name(),
                convertLocalDateTimeToDate(signUpCommand.accessTokenExpireIn()));
    }
}
