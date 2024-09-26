package kr.hoppang.adapter.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // 도메인 depth 순서로 작성
    INVALID_USER_INFO(10, "유저 정보가 정확하지 않습니다."),
    NOT_EXIST_USER(11, "유저가 존재하지 않습니다."),
    NOT_AUTHORIZED_USER(12, "접근 권한이 없습니다."),
    NOT_EXIST_DATA_IN_CACHE(13, "해당하는 캐시 데이터가 존재하지 않습니다."),

    LOGIN_FAIL(100, "로그인 실패, 아이디 또는 비밀번호를 확인해주세요."),
    EXPIRED_ACCESS_TOKEN(101, "만료 된 토큰입니다."),
    INVALID_SIGNUP_REQUEST_DUPLICATE_EMAIL(102, "이미 존재하는 이메일 입니다."),

    NOT_EXIST_CHASSIS_PRICE_INFO(200, "샤시 가격 정보가 존재하지않습니다."),
    NOT_EXIST_ADDITIONAL_CRITERIA_PRICE_INFO(201, "가격 필수 참고 기준값이 존재하지 않습니다."),
    NOT_AVAILABLE_MANUFACTURE(202, "너비/높이 최소 단위가 있으므로 제작이 불가합니다."),
    CHASSIS_TYPE_IS_MANDATORY(203, "샤시 종류 정보는 필수값 입니다."),
    COMPANY_TYPE_IS_MANDATORY(204, "샤시 회사 정보는 필수값 입니다.")
    ;

    private final int code;
    private final String message;
}
