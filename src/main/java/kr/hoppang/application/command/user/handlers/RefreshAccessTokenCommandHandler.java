package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.YET_EXPIRED_TOKEN;
import static kr.hoppang.adapter.common.util.CheckUtil.loginCheck;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commands.RefreshAccessTokenCommand;
import kr.hoppang.application.command.user.oauth.OAuthService;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.config.security.jwt.JWTUtil;
import kr.hoppang.domain.user.OauthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshAccessTokenCommandHandler implements
        ICommandHandler<RefreshAccessTokenCommand, String> {

    private final JWTUtil jwtUtil;
    private final List<OAuthService> oAuthServices;

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
    public String handle(final RefreshAccessTokenCommand command) {

        log.info("[핸들러 - 소셜 ({}) 로그인 토큰 갱신] RefreshAccessTokenCommand = {}", command.oauthType().getType(),
                command);

        Map<String, String> expiredInfo = jwtUtil.isExpiredReturnWithExpiredUserInfo(
                jwtUtil.getTokenWithoutBearer(command.expiredToken()));

        loginCheck(expiredInfo == null, YET_EXPIRED_TOKEN);

        String userEmail = expiredInfo.get("username");

        OAuthServiceLogInResultDto refreshingTokenInfo =
                oAuthServiceEnumMap.get(command.oauthType()).refreshAccessToken(userEmail);

        log.info("[핸들러 - 소셜 ({}) 로그인 토큰 갱신])", command.oauthType().getType());

        return jwtUtil.createJwtForSso(
                refreshingTokenInfo.email(),
                refreshingTokenInfo.userRole().name(),
                refreshingTokenInfo.oauthType().name(),
                refreshingTokenInfo.accessTokenExpireIn()
        );
    }
}
