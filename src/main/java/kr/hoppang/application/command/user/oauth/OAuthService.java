package kr.hoppang.application.command.user.oauth;

import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;

public interface OAuthService {

    OauthType getOauthType();

    SignUpCommand logIn(final String code, final String deviceId);

    OAuthServiceLogInResultDto refreshAccessToken(final String userEmail);
}
