package kr.hoppang.domain.user;

import java.util.Arrays;
import java.util.NoSuchElementException;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.Getter;

// 유저 역할
@Getter
public enum UserRole {
    ROLE_CUSTOMER, // 고객
    ROLE_BUSINESS_MAN, // 파트너사
    ROLE_ADMIN;// 관리자

    public static UserRole from(final String target) {
        return Arrays.stream(values())
                .filter(f->target.equals(f.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("UserRole 에 없는 필드 입니다."));
    }
}