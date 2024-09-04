package kr.hoppang.domain.user;

import lombok.Getter;

// 유저 역할
@Getter
public enum UserRole {
    ROLE_CUSTOMER, // 고객
    BUSINESS_MAN, // 파트너사
    ROLE_ADMIN;// 관리자
}