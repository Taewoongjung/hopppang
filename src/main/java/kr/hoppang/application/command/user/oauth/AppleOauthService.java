package kr.hoppang.application.command.user.oauth;

import static kr.hoppang.adapter.common.exception.ErrorType.PLEASE_LOGIN_AGAIN;
import static kr.hoppang.adapter.common.util.CheckUtil.expiredRefreshedTokenCheck;
import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import java.io.File;
import java.io.FileReader;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import kr.hoppang.application.command.user.oauth.dto.AppleIDTokenPayload;
import kr.hoppang.application.command.user.oauth.dto.AppleRefreshToken;
import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.application.command.user.oauth.dto.TokenResponse;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleOauthService implements OAuthService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    @Value(value = "${login.apple.client-id}")
    public String appleClientId;

    @Value(value = "${login.apple.redirect-uri}")
    private String appleRedirectUri;

    @Value(value = "${login.apple.key-id}")
    private String keyId;

    @Value(value = "${login.apple.team-id}")
    private String teamId;

    @Value(value = "${login.apple.private-key-path}")
    private String privateKeyPath;

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
    public OAuthLoginResultDto logIn(final String code) throws Exception {

        String tokenInfoFromApple = getTokenInfoFromApple(code);

        return getUserInfoFromAppleAndMakeUserObject(tokenInfoFromApple);
    }

    private String getTokenInfoFromApple(final String code) throws Exception {
        System.out.println("code = " + code);
        String clientSecret = generateJWT();
        System.out.println("clientSecret = " + clientSecret);

        return webClient.post()
                .uri("https://appleid.apple.com/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(
                        "grant_type", "authorization_code")
                        .with("client_id", appleClientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", appleRedirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private String generateJWT() throws Exception {
        PrivateKey pKey = generatePrivateKey();
        String token = Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setAudience("https://appleid.apple.com")
                .setSubject(appleClientId)
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.ES256, pKey)
                .compact();
        return token;
    }

    private PrivateKey generatePrivateKey() throws Exception {
        File file = ResourceUtils.getFile(privateKeyPath);
        final PEMParser pemParser = new PEMParser(new FileReader(file));
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        final PrivateKey pKey = converter.getPrivateKey(object);
        pemParser.close();
        return pKey;
    }

    private OAuthLoginResultDto getUserInfoFromAppleAndMakeUserObject(
            final String tokenInfoFromApple) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        TokenResponse tokenResponse = objectMapper.readValue(tokenInfoFromApple, TokenResponse.class);
        System.out.println("tokenResponse = " + tokenResponse);

        String idToken = tokenResponse.id_token();
        String payload = idToken.split("\\.")[1];
        System.out.println("payload = " + payload);

        String decoded = new String(Decoders.BASE64.decode(payload));
        System.out.println("decoded = " + decoded);

        AppleIDTokenPayload idTokenPayload = new Gson().fromJson(decoded, AppleIDTokenPayload.class);
        System.out.println("idTokenPayload = " + idTokenPayload);

        String userName = "호빵유저" + generateRandomNumber(9);

        long connectedAt = Long.parseLong(idTokenPayload.getAuth_time().toString());
        LocalDateTime connectedAtLocalDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(connectedAt),
                ZoneId.of("Asia/Seoul"));

        long accessTokenExpireTime = Long.parseLong(idTokenPayload.getExp().toString());
        LocalDateTime accessTokenExpireInLocalDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(accessTokenExpireTime),
                ZoneId.of("Asia/Seoul"));

        // 애플 로그인은 일단 기본 정보들은 애플에서 안불러오고 토큰까지만 발급 하고 추후 유저에게 따로 받아서 업데이트 한다.
        return new OAuthLoginResultDto(
                userName,
                null,
                idTokenPayload.getSub(),
                "",
                UserRole.ROLE_CUSTOMER,
                OauthType.APL,
                idTokenPayload.getSub(),
                connectedAtLocalDateTime,
                tokenResponse.access_token(),
                accessTokenExpireInLocalDateTime,
                tokenResponse.refresh_token(),
                LocalDateTime.now().plusYears(10L)
        );
    }

    private String generateRandomNumber(final int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthServiceLogInResultDto refreshAccessToken(final String userEmail) throws Exception {

        User user = userRepository.findByEmail(userEmail);

        checkIfLoggedInUserWithExpiredRefreshToken(user);

        AppleRefreshToken appleRefreshToken = getAccessTokenToRefresh(
                user.getTheLatestRefreshToken().getToken());

        String accessToken = appleRefreshToken.getAccess_token();

        // 애플은 리프레시 토큰에 유효시간이 없기 때문에 따로 시간 검증을 하지 않는다.
        long expireInSeconds = Long.parseLong(appleRefreshToken.getExpire_in().toString());
        LocalDateTime accessTokenExpireInLocalDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(expireInSeconds),
                ZoneId.of("Asia/Seoul"));

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
    private AppleRefreshToken getAccessTokenToRefresh(final String refreshToken) throws Exception {

        String clientSecret = generateJWT();

        String refreshedInfo = webClient.post()
                .uri("https://appleid.apple.com/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(
                                "grant_type", "refresh_token")
                        .with("client_id", appleClientId)
                        .with("client_secret", clientSecret)
                        .with("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(refreshedInfo, AppleRefreshToken.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawUser(final String userEmail) {
        return false;
    }
}
