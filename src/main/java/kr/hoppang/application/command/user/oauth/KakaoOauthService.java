package kr.hoppang.application.command.user.oauth;

import static kr.hoppang.adapter.common.exception.ErrorType.PLEASE_LOGIN_AGAIN;
import static kr.hoppang.adapter.common.util.CheckUtil.expiredRefreshedTokenCheck;
import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;
import static kr.hoppang.adapter.common.util.VersatileUtil.convertStringToLocalDateTime2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import kr.hoppang.util.deserializer.DateDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthService implements OAuthService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    @Value(value = "${login.kakao.rest-api-key}")
    public String restApiKey;

    @Value(value = "${login.kakao.redirect-uri}")
    public String redirectUri;

    @Override
    public OauthType getOauthType() {
        return OauthType.KKO;
    }

    @Override
    public String getReqLoginUrl() {

        String baseUrl = "https://kauth.kakao.com/oauth/authorize?";

        String clientId = "client_id=" + restApiKey;
        String redirection = "&redirect_uri=" + redirectUri;
        String extra = "&response_type=code";

        return baseUrl + clientId + redirection + extra + "&prompt=select_account";
    }


    @Override
    public OAuthLoginResultDto logIn(final String infoFromThirdPartyAuth) {

        return getUserInfoFromKakaoAndMakeUserObject(infoFromThirdPartyAuth);
    }

    private OAuthLoginResultDto getUserInfoFromKakaoAndMakeUserObject(final String tokenInfoFromKakao) {

        Map<String, Object> resultMap = getDataFromResponseJson(tokenInfoFromKakao);

        String accessToken = resultMap.get("accessToken").toString();
        String accessTokenExpireIn = resultMap.get("accessTokenExpiresAt").toString();
        String refreshToken = resultMap.get("refreshToken").toString();
        String refreshTokenExpireIn = resultMap.get("refreshTokenExpiresAt")
                .toString();

        String responseOfUserInfoFromKakao = getUserInfoFromKakao(accessToken);

        Map<String, Object> userInfo = getDataFromResponseJson(responseOfUserInfoFromKakao);

        Long providerUserId = convertObjectToLong(userInfo.get("id"));

        String connectedAt = userInfo.get("connected_at").toString();
        Object kakaoAccount = userInfo.get("kakao_account");

        Map<String, Object> accountInfo = getDataFromResponseJson(
                JSONStringer.valueToString(kakaoAccount));

        Map<String, Object> userProfile = getDataFromResponseJson(
                JSONStringer.valueToString(accountInfo.get("profile")));

        String userEmail = accountInfo.get("email").toString();
        String userName = "";

        if (userProfile != null) {
            userName = userProfile.get("nickname") != null ? userProfile.get("nickname").toString()
                    : "호빵유저" + generateRandomNumber(9);
        } else {
            userName = "호빵유저" + generateRandomNumber(9);
        }

        LocalDateTime connectedAtLocalDateTime = convertStringToLocalDateTime2(connectedAt);
        LocalDateTime accessTokenExpireInLocalDateTime = connectedAtLocalDateTime.plusSeconds(
                (long) Double.parseDouble(accessTokenExpireIn));
        LocalDateTime refreshTokenExpireInLocalDateTime = connectedAtLocalDateTime.plusSeconds(
                (long) Double.parseDouble(refreshTokenExpireIn));

        return new OAuthLoginResultDto(
                userName,
                null,
                userEmail,
                "",
                UserRole.ROLE_CUSTOMER,
                OauthType.KKO,
                providerUserId.toString(),
                connectedAtLocalDateTime,
                accessToken,
                accessTokenExpireInLocalDateTime,
                refreshToken,
                refreshTokenExpireInLocalDateTime);
    }

    private String generateRandomNumber(final int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String getUserInfoFromKakao(final String accessToken) {
        return webClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    // 에러 처리 로직
                    log.error("Error occurred while processing Kakao response: ", e);
                    return Mono.empty();
                }).block();
    }

    public Map<String, Object> getDataFromResponseJson(final String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        Gson gson = gsonBuilder.create();

        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        return gson.fromJson(response, mapType);
    }

    private Long convertObjectToLong(final Object target) {
        Long providerUserId;

        // Double 타입으로 들어 오는 id 값 온전히 변환하기
        if (target instanceof Number) {
            // 숫자 타입인 경우
            BigDecimal bd = new BigDecimal(target.toString());
            providerUserId = bd.longValue();
        } else if (target instanceof String) {
            // 문자열인 경우
            BigDecimal bd = new BigDecimal((String) target);
            providerUserId = bd.longValue();
        } else {
            // 예상치 못한 타입인 경우
            throw new IllegalArgumentException("Unexpected type for provider ID");
        }

        return providerUserId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthServiceLogInResultDto refreshAccessToken(final String userEmail) {

        User user = userRepository.findByEmail(userEmail);

        checkIfLoggedInUserWithExpiredRefreshToken(user);

        Map<String, Object> refreshedInfo = getAccessTokenToRefresh(
                user.getTheLatestRefreshToken().getToken());

        String accessToken = refreshedInfo.get("access_token").toString();
        String accessTokenExpireIn = refreshedInfo.get("expires_in").toString();

        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime nowInSeoul = LocalDateTime.now(seoulZoneId);
        LocalDateTime accessTokenExpireInLocalDateTime = nowInSeoul.plusSeconds(
                (long) Double.parseDouble(accessTokenExpireIn));

        user.reviseTheLatestAccessToken(accessToken, accessTokenExpireInLocalDateTime);

        userRepository.updateToken(user.getEmail(), TokenType.ACCESS, accessToken,
                accessTokenExpireInLocalDateTime);

        return new OAuthServiceLogInResultDto(
                user.getEmail(),
                user.getUserRole(),
                user.getOauthType(),
                convertLocalDateTimeToDate(accessTokenExpireInLocalDateTime));
    }

    private void checkIfLoggedInUserWithExpiredRefreshToken(final User isUserExist) {
        UserToken userToken = isUserExist.getTheLatestRefreshToken();

        boolean isRefreshTokenExpired = userToken.getExpireIn().isBefore(LocalDateTime.now());

        if (isRefreshTokenExpired) {
            userRepository.updateRequiredReLogin(isUserExist.getEmail());
        }

        expiredRefreshedTokenCheck(isRefreshTokenExpired, PLEASE_LOGIN_AGAIN);
    }

    // 리프레스 토큰으로 엑세스 토큰 갱신하기
    private Map<String, Object> getAccessTokenToRefresh(final String refreshToken) {
        Mono<String> response = webClient.post().uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", restApiKey)
                        .with("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    // 에러 처리 로직
                    log.error("Error occurred while processing Kakao response: ", e);
                    return Mono.empty(); // 또는 기본값 반환
                });

        return getDataFromResponseJson(response.block());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawUser(final long userId) {

        User user = userRepository.findById(userId);
        UserToken userAccessToken = user.getTheLatestAccessToken();

        ResponseEntity<String> response = webClient.post().uri("https://kapi.kakao.com/v1/user/unlink")
                .headers(headers -> {
                    headers.add("Authorization", "Bearer " + userAccessToken.getToken());
                    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
                })
                .retrieve()
                .toEntity(String.class).block();

        System.out.println("response = " + response);

        return response != null && response.getStatusCode().value() == 200;
    }
}

