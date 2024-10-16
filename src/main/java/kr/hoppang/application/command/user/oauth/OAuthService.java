package kr.hoppang.application.command.user.oauth;

import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;

public interface OAuthService {

    OauthType getOauthType();

    OAuthLoginResultDto logIn(final String code);

    OAuthServiceLogInResultDto refreshAccessToken(final String userEmail);
}
