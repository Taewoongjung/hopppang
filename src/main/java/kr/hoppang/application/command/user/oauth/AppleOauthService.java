package kr.hoppang.application.command.user.oauth;

import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleOauthService implements OAuthService {

    @Value(value = "${login.apple.client-id}")
    public String appleClientId;

    @Value(value = "${login.apple.redirect-uri}")
    private String appleRedirectUri;

    @Override
    public OauthType getOauthType() {
        return OauthType.APL;
    }

    @Override
    public String getReqLoginUrl() {
        String baseUrl = "https://appleid.apple.com/auth/authorize?";

        String responseType = "response_type=code";
        String clientId = "&client_id=" + appleClientId;
        String redirectUri = "&redirect_uri=" + appleRedirectUri;

        return baseUrl + responseType + clientId + redirectUri;
    }

    @Override
    public OAuthLoginResultDto logIn(String code) {
        return null;
    }

    @Override
    public OAuthServiceLogInResultDto refreshAccessToken(String userEmail) {
        return null;
    }
}
