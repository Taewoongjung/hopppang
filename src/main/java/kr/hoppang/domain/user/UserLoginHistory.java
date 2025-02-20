package kr.hoppang.domain.user;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserLoginHistory {

    private final Long id;
    private final Long userId;
    private final LocalDateTime createdAt;

    private UserLoginHistory(
            final Long id,
            final Long userId,
            final LocalDateTime createdAt
    ) {

        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // 조회
    public static UserLoginHistory of(
            final Long id,
            final Long userId,
            final LocalDateTime createdAt
    ) {
        return new UserLoginHistory(id, userId, createdAt);
    }

    // 생성
    public static UserLoginHistory of(
            final Long userId,
            final LocalDateTime createdAt
    ) {
        return new UserLoginHistory(null, userId, createdAt);
    }
}
