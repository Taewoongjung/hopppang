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
import jakarta.annotation.PostConstruct;
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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    private String secretKey;

    @PostConstruct
    @Scheduled(cron = "0 0 1 * 3 *", zone = "Asia/Seoul")
        // 3개월에 한 번씩 secretKey 갱신
    void createSecretKey() throws Exception {
        secretKey = generateJWT();
    }

    private String generateJWT() throws Exception {
        PrivateKey pKey = generatePrivateKey();
        String token = Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setAudience("https://appleid.apple.com")
                .setSubject(appleClientId)
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 100)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(pKey, SignatureAlgorithm.ES256)
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

    private String getTokenInfoFromApple(final String code) {
        System.out.println("code = " + code);
        System.out.println("secretKey = " + secretKey);

        return webClient.post()
                .uri("https://appleid.apple.com/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(
                                "grant_type", "authorization_code")
                        .with("client_id", appleClientId)
                        .with("client_secret", secretKey)
                        .with("redirect_uri", appleRedirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private OAuthLoginResultDto getUserInfoFromAppleAndMakeUserObject(
            final String tokenInfoFromApple) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        TokenResponse tokenResponse = objectMapper.readValue(tokenInfoFromApple,
                TokenResponse.class);

        String idToken = tokenResponse.id_token();
        String payload = idToken.split("\\.")[1];

        String decoded = new String(Decoders.BASE64.decode(payload));

        AppleIDTokenPayload idTokenPayload = new Gson().fromJson(decoded,
                AppleIDTokenPayload.class);

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

    /**
     * @NOTE 애플은 리프레시 토큰의 유효기간이 없으므로 호빵 서비스 자체 내에서 검증한다. 만약 유저가 회원탈퇴 버튼을 누르면 해당 토큰들을 삭제하는 방식으로 한다.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OAuthServiceLogInResultDto refreshAccessToken(final String userEmail) {

        log.info("애플 리프레시 토큰 발급 = {}", userEmail);

        User user = userRepository.findByEmail(userEmail);

        checkIfLoggedInUserWithExpiredRefreshToken(user);

        LocalDateTime refreshedAccessTokenExpireIn = getAccessTokenToRefresh();

        userRepository.updateToken(user.getEmail(), TokenType.ACCESS, null,
                refreshedAccessTokenExpireIn);

        return new OAuthServiceLogInResultDto(
                user.getId(),
                user.getEmail(),
                user.getUserRole(),
                user.getOauthType(),
                convertLocalDateTimeToDate(refreshedAccessTokenExpireIn));
    }

    private void checkIfLoggedInUserWithExpiredRefreshToken(final User isUserExist) {
        UserToken userToken = isUserExist.getTheLatestRefreshToken();

        boolean isRefreshTokenExpired = userToken.getExpireIn().isBefore(LocalDateTime.now());

        expiredRefreshedTokenCheck(isRefreshTokenExpired, PLEASE_LOGIN_AGAIN);

        userRepository.updateRequiredReLogin(isUserExist.getEmail());
    }

    // 리프레스 토큰으로 엑세스 토큰 갱신하기
    private LocalDateTime getAccessTokenToRefresh() {

        return LocalDateTime.now().plusHours(3);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean withdrawUser(final long userId) {

        User user = userRepository.findById(userId);
        UserToken userRefreshToken = user.getTheLatestRefreshToken();

        ResponseEntity<String> response = webClient.post()
                .uri("https://appleid.apple.com/auth/revoke")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData("client_id", appleClientId)
                                .with("client_secret", secretKey)
                                .with("token_type_hint", "refresh_token")
                                .with("token", userRefreshToken.getToken()))
                .retrieve()
                .toEntity(String.class).block();

        return response != null && response.getStatusCode().value() == 200;
    }
}