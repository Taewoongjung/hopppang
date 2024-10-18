package kr.hoppang.util.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleAuthUtil {

    @Value(value = "${login.apple.client-id}")
    public String appleClientId;

    @Value(value = "${login.apple.redirect-uri}")
    private String appleRedirectUri;

    public String getReqLoginUrl() {
        String baseUrl = "https://appleid.apple.com/auth/authorize?";

        String responseType = "response_type=code";
        String clientId = "&client_id=" + appleClientId;
        String redirectUri = "&redirect_uri=" + appleRedirectUri;

        return baseUrl + responseType + clientId + redirectUri;
    }
}
