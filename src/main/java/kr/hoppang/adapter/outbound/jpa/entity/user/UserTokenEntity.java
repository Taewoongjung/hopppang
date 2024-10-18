package kr.hoppang.adapter.outbound.jpa.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.UserToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "user_token_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(name = "provider_user_id", nullable = false, columnDefinition = "varchar(255)")
    private String providerUserId; // 소셜로그인 서비스의 유니크한 고객 id

    @Enumerated(EnumType.STRING)
    @Column(name = "tokenType", nullable = false, columnDefinition = "char(7)")
    private TokenType tokenType;

    @Column(name = "token", nullable = false, columnDefinition = "varchar(255)")
    private String token;

    @Column(name = "connected_at", nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime connectedAt;

    @Column(name = "expire_in", nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime expireIn;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    private UserTokenEntity(
            final Long id,
            final Long userId,
            final String providerUserId,
            final TokenType tokenType,
            final String token,
            final LocalDateTime connectedAt,
            final LocalDateTime expireIn
    ) {
        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.userId = userId;
        this.providerUserId = providerUserId;
        this.tokenType = tokenType;
        this.token = token;
        this.connectedAt = connectedAt;
        this.expireIn = expireIn;
    }

    public static UserTokenEntity of(
            final Long userId,
            final String providerUserId,
            final TokenType tokenType,
            final String token,
            final LocalDateTime connectedAt,
            final LocalDateTime expireIn
    ) {
        return new UserTokenEntity(null, userId, providerUserId, tokenType, token, connectedAt, expireIn);
    }

    public static UserTokenEntity of(
            final String providerUserId,
            final TokenType tokenType,
            final String token,
            final LocalDateTime connectedAt,
            final LocalDateTime expireIn
    ) {
        return new UserTokenEntity(null, null, providerUserId, tokenType, token, connectedAt, expireIn);
    }

    public UserToken toPojo() {
        return UserToken.of(
                this.id,
                this.userId,
                this.providerUserId,
                this.tokenType,
                this.token,
                this.connectedAt,
                this.expireIn,
                this.getCreatedAt(),
                this.getLastModified());
    }

    public void reviseToken(final String newToken, final LocalDateTime newExpireIn) {
        this.token = newToken;
        this.expireIn = newExpireIn;
        this.updateLastModifiedAsNow();
    }

    public void updateTokenAndExpireTime(final String updateToken, final LocalDateTime updateTime) {
        this.token = updateToken;
        this.expireIn = updateTime;
    }
}
