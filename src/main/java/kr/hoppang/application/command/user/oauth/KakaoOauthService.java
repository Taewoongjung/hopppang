package kr.hoppang.application.command.user.oauth;

import static kr.hoppang.adapter.common.exception.ErrorType.PLEASE_LOGIN_AGAIN;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;
import static kr.hoppang.adapter.common.util.VersatileUtil.convertStringToLocalDateTime2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthService implements OAuthService {

    private final WebClient webClient;
    private final UserRepository userRepository;

    @Value(value = "${login.kakao.rest-api-key}")
    public String restApiKey;

    @Value(value = "${login.kakao.redirect-uri}")
    public String redirectUri;


    @Override
    public OauthType getOauthType() {
        return OauthType.KKO;
    }

    @Override
    public SignUpCommand logIn(final String code, final String deviceId) {

        String tokenInfoFromKakao = getTokenInfoFromKakao(code);

        return getUserInfoFromKakaoAndMakeUserObject(tokenInfoFromKakao, deviceId);
    }

    private String getTokenInfoFromKakao(final String code) {
        return webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", restApiKey)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private SignUpCommand getUserInfoFromKakaoAndMakeUserObject(
            final String tokenInfoFromKakao,
            final String deviceId
    ) {

        Map<String, Object> resultMap = getDataFromResponseJson(tokenInfoFromKakao);

        String accessToken = resultMap.get("access_token").toString();
        String accessTokenExpireIn = resultMap.get("expires_in").toString();
        String refreshToken = resultMap.get("refresh_token").toString();
        String refreshTokenExpireIn = resultMap.get("refresh_token_expires_in")
                .toString();

        Mono<String> responseOfUserInfoFromKakao = webClient.post()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(e -> {
                // 에러 처리 로직
                log.error("Error occurred while processing Kakao response: ", e);
                return Mono.empty(); // 또는 기본값 반환
            });

        Map<String, Object> userInfo = getDataFromResponseJson(
                responseOfUserInfoFromKakao.block());

        Long providerUserId = convertObjectToLong(userInfo.get("id"));

        String connectedAt = userInfo.get("connected_at").toString();
        Object kakaoAccount = userInfo.get("kakao_account");

        Map<String, Object> accountInfo = getDataFromResponseJson(
                JSONStringer.valueToString(kakaoAccount));

        String userName = accountInfo.get("name").toString();
        String userEmail = accountInfo.get("email").toString();
        String userTelNumber = formatPhoneNumber(accountInfo.get("phone_number").toString());

        LocalDateTime connectedAtLocalDateTime = convertStringToLocalDateTime2(connectedAt);
        LocalDateTime accessTokenExpireInLocalDateTime = connectedAtLocalDateTime.plusSeconds(
                (long) Double.parseDouble(accessTokenExpireIn));
        LocalDateTime refreshTokenExpireInLocalDateTime = connectedAtLocalDateTime.plusSeconds(
                (long) Double.parseDouble(refreshTokenExpireIn));

//        SignUpCommand signUpCommand = new SignUpCommand(
        return new SignUpCommand(
                userName,
                null,
                userEmail,
                userTelNumber,
                UserRole.ROLE_CUSTOMER,
                OauthType.KKO,
                deviceId,
                providerUserId.toString(),
                convertStringToLocalDateTime2(connectedAt),
                accessToken,
                accessTokenExpireInLocalDateTime,
                refreshToken,
                refreshTokenExpireInLocalDateTime);

//        User registeredUser = signUpCommandHandler.handle(signUpCommand);
//
//        return new ResultOfSsoLogin(registeredUser.getEmail(), registeredUser.getUserRole(),
//                registeredUser.getOauthType(), accessTokenExpireInLocalDateTime);
    }

    private Map<String, Object> getDataFromResponseJson(final String response) {
        Gson gson = new Gson();
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

    // +82 10-0000-000 이렇게 들어 오는 전화번호 데이터를 정제한다
    private String formatPhoneNumber(final String phoneNumber) {
        // 입력된 전화번호에서 특수문자 제거
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]", "");

        // 국가 코드 제거 (맨 앞의 82 제거)
        if (cleanedNumber.startsWith("82")) {
            cleanedNumber = cleanedNumber.substring(2);
        }

        // 맨 앞에 0 추가
        if (!cleanedNumber.startsWith("0")) {
            cleanedNumber = "0" + cleanedNumber;
        }

        return cleanedNumber;
    }

    @Override
    public OAuthServiceLogInResultDto refreshAccessToken(final String userEmail) {

        User user = userRepository.findByEmail(userEmail);

        check(user.isLatestRefreshTokenValid(), PLEASE_LOGIN_AGAIN);

        Map<String, Object> refreshedInfo = getAccessTokenToRefresh(
                user.getTheLatestRefreshToken());

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
}

