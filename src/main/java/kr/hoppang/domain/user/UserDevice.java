package kr.hoppang.domain.user;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserDevice {

    private Long id;
    private Long userId;
    private String deviceType;
    private String deviceId;
    private LocalDateTime createdAt;

    public UserDevice(
            final Long id,
            final Long userId,
            final String deviceType,
            final String deviceId,
            final LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.createdAt = createdAt;
    }

    // 생성
    public static UserDevice of(
            final String deviceType,
            final String deviceId
    ) {
        return new UserDevice(null, null, deviceType, deviceId, null);
    }

    // 조회
    public static UserDevice of(
            final Long id,
            final Long userId,
            final String deviceType,
            final String deviceId,
            final LocalDateTime createdAt
    ) {
        return new UserDevice(id, userId, deviceType, deviceId, createdAt);
    }
}
