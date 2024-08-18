package kr.hoppang.domain.user;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 유저 역할
@Getter
@RequiredArgsConstructor
public enum UserRole {
    CUSTOMER("CUSTOMER"), // 고객
    BUSINESS_MAN("PARTNER"), // 파트너사
    ADMIN("ADMIN");// 관리자

    private final String desc;

    public static UserRole from(final String target) {
        return Arrays.stream(values()).filter(f -> target.equals(f.desc)).findFirst().orElse(null);
    }
}