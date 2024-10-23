package kr.hoppang.application.command.user.oauth;

import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;

public interface OAuthService {

    OauthType getOauthType();

    String getReqLoginUrl();

    OAuthLoginResultDto logIn(final String code) throws Exception;

    OAuthServiceLogInResultDto refreshAccessToken(final String userEmail) throws Exception;

    boolean withdrawUser(final String userEmail);
}
