package kr.hoppang.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import kr.hoppang.domain.user.OauthType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret-key}") String secret) {

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(final String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(final String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public OauthType getOauthType(final String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("oAuthType", OauthType.class);
    }

    public boolean isExpired(final String token) {
        try {
            // 여기서 토큰이 만료되었는지 검증
            Date expiration = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
            log.info("expiration comparison date = {}", expiration);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Map<String, String> isExpiredReturnWithExpiredUserInfo(final String token) {
        Map<String, String> expiredUserInfo = new HashMap<>();
        try {
            // 여기서 토큰이 만료되었는지 검증
            Date expiration = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
            boolean isExpired = expiration.before(new Date());
            return isExpired ? new HashMap<>() : null;
        } catch (ExpiredJwtException e) {
            // 완료 된 토큰에 있는 정보 축출
            Claims claims = e.getClaims();

            expiredUserInfo.put("oAuthType", claims.get("oAuthType", String.class));
            expiredUserInfo.put("username", claims.get("username", String.class));
            return expiredUserInfo;
        }
    }


    public String createJwt(
            final String userName,
            final String role,
            final OauthType oauthType,
            final Long expiredMs
    ) {

        String response = Jwts.builder()
                .claim("username", userName)
                .claim("role", role)
                .claim("oAuthType", oauthType.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();

        log.info("jwt = {}", response);

        return response;
    }

    public String createJwtForSso(
            final String userName,
            final String role,
            final String oauthType,
            final Date expireMs
    ) {
        log.info("jwt expire time = {}", expireMs);

        String response = Jwts.builder()
                .claim("username", userName)
                .claim("role", role)
                .claim("oAuthType", oauthType)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expireMs)
                .signWith(secretKey)
                .compact();

        log.info("jwt for sso = {}", response);

        return response;
    }

    public String getTokenWithoutBearer(final String authorization) {
        return authorization.split(" ")[1];
    }
}
