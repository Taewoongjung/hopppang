package kr.hoppang.application.command.user.oauth;

import static kr.hoppang.adapter.common.exception.ErrorType.PLEASE_LOGIN_AGAIN;
import static kr.hoppang.adapter.common.exception.ErrorType.PLEASE_TRY_LATER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.adapter.common.util.CheckUtil.expiredRefreshedTokenCheck;
import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Map;
import kr.hoppang.application.command.user.oauth.dto.GoogleUserResource;
import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class GoogleOauthService implements OAuthService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    @Value(value = "${login.google.client-id}")
    public String googleClientId;

    @Value(value = "${login.google.client-secret}")
    public String googleClientSecret;

    @Value(value = "${login.google.redirect-uri}")
    private String googleRedirectUri;

    @Override
    public OauthType getOauthType() {
        return OauthType.GLE;
    }

    @Override
    public String getReqLoginUrl() {
        String baseUrl = "https://accounts.google.com/o/oauth2/v2/auth";

        String clientId = "?client_id=" + googleClientId;
        String redirectUri = "&redirect_uri=" + googleRedirectUri;
        String accessType = "&access_type=offline";
        String responseType = "&response_type=code";
        String scope = "&scope=email";

        return baseUrl + clientId + redirectUri + responseType + accessType + scope;
    }

    @Override
    public OAuthLoginResultDto logIn(final String code) throws Exception {

        String tokenInfoFromGoogle = getTokenInfoFromApple(code);

        return getUserInfoFromGoogleAndMakeUserObject(tokenInfoFromGoogle);
    }

    private String getTokenInfoFromApple(final String code) {
        return webClient.post()
                .uri("https://oauth2.googleapis.com/token?access_type=offline")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(
                                "grant_type", "authorization_code")
                        .with("client_id", googleClientId)
                        .with("client_secret", googleClientSecret)
                        .with("redirect_uri", googleRedirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private OAuthLoginResultDto getUserInfoFromGoogleAndMakeUserObject(
            final String tokenInfoFromApple) throws JsonProcessingException {

        Map<String, Object> tokenResponse = getDataFromResponseJson(tokenInfoFromApple);

        GoogleUserResource userResource = getUserResource(tokenResponse.get("access_token").toString());
        String userName = "호빵유저" + generateRandomNumber(9);

        log.info("tokenResponse = {}", tokenResponse);

        LocalDateTime accessTokenExpireInLocalDateTime = null;
        if (tokenResponse.get("expires_in") != null) {
            long accessTokenExpireTime = ((Double) tokenResponse.get("expires_in")).longValue();
            accessTokenExpireInLocalDateTime = LocalDateTime.now()
                    .plusSeconds(accessTokenExpireTime);
        }

        return new OAuthLoginResultDto(
                userName,
                null,
                userResource.email(),
                "",
                UserRole.ROLE_CUSTOMER,
                OauthType.GLE,
                userResource.id(),
                LocalDateTime.now(),
                tokenResponse.get("access_token") != null ?
                        tokenResponse.get("access_token").toString()
                        : null,
                accessTokenExpireInLocalDateTime,
                tokenResponse.get("refresh_token") != null ?
                        tokenResponse.get("refresh_token").toString()
                        : null,
                LocalDateTime.now().plusMonths(6L)
        );
    }

    private String generateRandomNumber(final int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Map<String, Object> getDataFromResponseJson(final String response) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        return gson.fromJson(response, mapType);
    }

    private GoogleUserResource getUserResource(final String accessToken)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        String userInfoFromGoogle = webClient.get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class).onErrorResume(e -> {
                    // 에러 처리 로직
                    log.error("Error occurred while processing Kakao response: ", e);
                    return Mono.empty();
                }).block();

        log.info("userInfoFromGoogle = {}", userInfoFromGoogle);

        return objectMapper.readValue(userInfoFromGoogle, GoogleUserResource.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthServiceLogInResultDto refreshAccessToken(final String userEmail) {

        log.info("구글 리프레시 토큰 발급 = {}", userEmail);

        User user = userRepository.findByEmail(userEmail);

        checkIfLoggedInUserWithExpiredRefreshToken(user);

        Map<String, Object> refreshedInfo = getAccessTokenToRefresh(user);

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

    private void checkIfLoggedInUserWithExpiredRefreshToken(final User targetUser) {
        UserToken userToken = targetUser.getTheLatestRefreshToken();

        boolean isRefreshTokenExpired = userToken.getExpireIn().isBefore(LocalDateTime.now());

        if (isRefreshTokenExpired) {
            userRepository.updateRequiredReLogin(targetUser.getEmail());
        }

        expiredRefreshedTokenCheck(isRefreshTokenExpired, PLEASE_LOGIN_AGAIN);
    }

    private void checkIfLoggedInUserWithExpiredAccessToken(final User targetUser) {
        UserToken userToken = targetUser.getTheLatestAccessToken();

        boolean isRefreshTokenExpired = userToken.getExpireIn().isBefore(LocalDateTime.now());

        if (isRefreshTokenExpired) {
            userRepository.updateRequiredReLogin(targetUser.getEmail());
        }

        check(isRefreshTokenExpired, PLEASE_TRY_LATER);
    }

    // 리프레스 토큰으로 엑세스 토큰 갱신하기
    private Map<String, Object> getAccessTokenToRefresh(final User user) {

        String response = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(
                                "grant_type", "refresh_token")
                        .with("client_id", googleClientId)
                        .with("client_secret", googleClientSecret)
                        .with("refresh_token", user.getTheLatestRefreshToken().getToken()))
                .retrieve()
                .bodyToMono(String.class).block();

        return getDataFromResponseJson(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawUser(final long userId) {

        User user = userRepository.findById(userId);

        checkIfLoggedInUserWithExpiredRefreshToken(user);
        checkIfLoggedInUserWithExpiredAccessToken(user);

        String accessToken = user.getTheLatestAccessToken().getToken();

        ResponseEntity<String> response = webClient.post()
                .uri("https://oauth2.googleapis.com/revoke?token=" + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(String.class).block();

        return response != null && response.getStatusCode().value() == 200;
    }
}
