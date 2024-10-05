package kr.hoppang.util.auth.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoAuthUtil {

    @Value(value = "${login.kakao.rest-api-key}")
    public String restApiKey;

    @Value(value = "${login.kakao.redirect-uri}")
    public String redirectUri;


    public String getReqLoginUrl() {
        String baseUrl = "https://kauth.kakao.com/oauth/authorize?";

        String clientId = "client_id=" + restApiKey;
        String redirection = "&redirect_uri=" + redirectUri;
        String extra = "&response_type=code";

        return baseUrl + clientId + redirection + extra;
    }
}
