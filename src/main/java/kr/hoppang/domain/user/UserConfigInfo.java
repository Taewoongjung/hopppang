package kr.hoppang.domain.user;

import java.time.LocalDateTime;
import kr.hoppang.util.common.BoolType;
import lombok.Getter;

@Getter
public class UserConfigInfo {

    private Long id;
    private Long userId;
    private BoolType isPushOn;
    private BoolType isAlimTalkOn;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    private UserConfigInfo(
            final Long id,
            final Long userId,
            final BoolType isPushOn,
            final BoolType isAlimTalkOn,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        this.id = id;
        this.userId = userId;
        this.isPushOn = isPushOn;
        this.isAlimTalkOn = isAlimTalkOn;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    // 생성
    public static UserConfigInfo of(
            final Long userId,
            final BoolType isPushOn,
            final BoolType isAlimTalkOn
    ) {
        return new UserConfigInfo(null, userId, isPushOn, isAlimTalkOn, null, null);
    }

    // 조회
    public static UserConfigInfo of(
            final Long id,
            final Long userId,
            final BoolType isPushOn,
            final BoolType isAlimTalkOn,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        return new UserConfigInfo(id, userId, isPushOn, isAlimTalkOn, createdAt, lastModified);
    }

    public boolean getIsPushOnAsBoolean() {
        if (BoolType.T.equals(this.isPushOn)) {
            return true;
        }

        return false;
    }
}
