package kr.hoppang.application.command.user.handlers;

import java.util.Map;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commands.AddUserLoginHistoryCommand;
import kr.hoppang.application.command.user.commands.RefreshAccessTokenCommand;
import kr.hoppang.application.command.user.oauth.OAuthServiceAdapter;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.config.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshAccessTokenCommandHandler implements
        ICommandHandler<RefreshAccessTokenCommand, String> {

    private final JWTUtil jwtUtil;
    private final OAuthServiceAdapter oAuthServiceAdapter;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public String handle(final RefreshAccessTokenCommand command) throws Exception {

        log.info("[핸들러 - 소셜 ({}) 로그인 토큰 갱신] RefreshAccessTokenCommand = {}", command.oauthType().getType(),
                command);

        Map<String, String> expiredInfo = jwtUtil.isExpiredReturnWithExpiredUserInfo(
                jwtUtil.getTokenWithoutBearer(command.expiredToken()));

        log.info("expiredInfo = {}", expiredInfo);

        String userEmail = expiredInfo.get("username");

        OAuthServiceLogInResultDto refreshingTokenInfo = oAuthServiceAdapter.refreshAccessToken(
                command.oauthType(), userEmail);

        log.info("[핸들러 - 소셜 ({}) 로그인 토큰 갱신])", command.oauthType().getType());

        eventPublisher.publishEvent(
                AddUserLoginHistoryCommand.builder()
                        .userId(refreshingTokenInfo.userId())
                        .build()
        );

        return jwtUtil.createJwtForSso(
                refreshingTokenInfo.email(),
                refreshingTokenInfo.userRole().name(),
                refreshingTokenInfo.oauthType().name(),
                refreshingTokenInfo.accessTokenExpireIn()
        );
    }
}
