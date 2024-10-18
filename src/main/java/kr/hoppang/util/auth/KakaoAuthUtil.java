package kr.hoppang.util.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoAuthUtil {

    @Value(value = "${login.kakao.rest-api-key}")
    public String kakaoRestApiKey;

    @Value(value = "${login.kakao.redirect-uri}")
    public String kakaoRedirectUri;


    public String getReqLoginUrl() {
        String baseUrl = "https://kauth.kakao.com/oauth/authorize?";

        String clientId = "client_id=" + kakaoRestApiKey;
        String redirection = "&redirect_uri=" + kakaoRedirectUri;
        String extra = "&response_type=code";

        return baseUrl + clientId + redirection + extra;
    }
}
