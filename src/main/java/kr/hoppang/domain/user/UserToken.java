package kr.hoppang.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserToken {

    private Long id;
    private Long userId;
    private String providerUserId;
    private TokenType tokenType;
    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime connectedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expireIn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModified;

    private UserToken(
            final Long id,
            final Long userId,
            final String providerUserId,
            final TokenType tokenType,
            final String token,
            final LocalDateTime connectedAt,
            final LocalDateTime expireIn,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        this.id = id;
        this.userId = userId;
        this.providerUserId = providerUserId;
        this.tokenType = tokenType;
        this.token = token;
        this.connectedAt = connectedAt;
        this.expireIn = expireIn;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    public static UserToken of(
            final String providerUserId,
            final TokenType tokenType,
            final String token,
            final LocalDateTime connectedAt,
            final LocalDateTime expireIn
    ) {
        return new UserToken(null, null, providerUserId, tokenType, token, connectedAt, expireIn, null, null);
    }

    public static UserToken of(
            final Long userId,
            final String providerUserId,
            final TokenType tokenType,
            final String token,
            final LocalDateTime connectedAt,
            final LocalDateTime expireIn
    ) {
        return new UserToken(null, userId, providerUserId, tokenType, token, connectedAt, expireIn, null, null);
    }

    public static UserToken of(
            final Long id,
            final Long userId,
            final String providerUserId,
            final TokenType tokenType,
            final String token,
            final LocalDateTime connectedAt,
            final LocalDateTime expireIn,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        return new UserToken(id, userId, providerUserId, tokenType, token, connectedAt, expireIn, createdAt,
                lastModified);
    }

    public void reviseToken(final String newToken, final LocalDateTime newExpireIn) {
        this.token = newToken;
        this.expireIn = newExpireIn;
    }
}
