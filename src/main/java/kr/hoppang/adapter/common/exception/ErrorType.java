package kr.hoppang.adapter.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // 도메인 depth 순서로 작성
    YET_EXPIRED_TOKEN(3, "아직 유효한 토큰입니다."),
    NOT_EXIST_TOKEN(4, "존재하지 않는 토큰 입니다."),
    NOT_EXIST_OAUTH_TYPE(5, "소셜 로그인 타입이 없습니다."),
    NOT_EXIST_ACCESS_TOKEN(6, "존재하지 않는 액세스 토큰 입니다."),
    NOT_EXIST_REFRESH_TOKEN(6, "존재하지 않는 리프래시 토큰 입니다."),
    PLEASE_LOGIN_AGAIN(7, "다시 로그인을 해주세요."),
    NO_HISTORY_PUBLISHED_REFRESH_TOKEN(8, "리프래쉬 토큰이 발급된 이력이 없습니다."),
    INVALID_OAUTH_SERVICE_TYPE(9, "지원하지 않는 소셜 로그인방식 입니다."),
    INVALID_USER_INFO(10, "유저 정보가 정확하지 않습니다."),
    NOT_EXIST_USER(11, "유저가 존재하지 않습니다."),
    NOT_AUTHORIZED_USER(12, "접근 권한이 없습니다."),
    NOT_EXIST_DATA_IN_CACHE(13, "해당하는 캐시 데이터가 존재하지 않습니다."),

    LOGIN_FAIL(100, "로그인 실패, 아이디 또는 비밀번호를 확인해주세요."),
    EXPIRED_ACCESS_TOKEN(101, "만료 된 토큰입니다."),
    INVALID_SIGNUP_REQUEST_DUPLICATE_EMAIL(102, "이미 존재하는 이메일 입니다."),
    INVALID_SIGNUP_REQUEST_DUPLICATE_TEL(103, "이미 존재하는 휴대폰번호 입니다."),
    FAIL_WHILE_LOGIN(104, "로그인중 에러가 발생했습니다."),

    NOT_EXIST_CHASSIS_PRICE_INFO(200, "샤시 가격 정보가 존재하지않습니다."),
    NOT_EXIST_ADDITIONAL_CRITERIA_PRICE_INFO(201, "가격 필수 참고 기준값이 존재하지 않습니다."),
    NOT_AVAILABLE_MANUFACTURE(202, "너비/높이 최소 단위가 있으므로 제작이 불가합니다."),
    CHASSIS_TYPE_IS_MANDATORY(203, "샤시 종류 정보는 필수값 입니다."),
    COMPANY_TYPE_IS_MANDATORY(204, "샤시 회사 정보는 필수값 입니다."),
    NOT_6DIGIT_VERIFY_NUMBER(205, "6자리 인증숫자가 아닙니다."),
    EMPTY_VERIFY_NUMBER(206, "인증숫자가 빈값입니다."),


    UNABLE_TO_SEND_SMS(300, "SMS 보내기에 실패했습니다."),
    INVALID_PHONE_CHECK_NUMBER(301, "sms 인증에 실패하였습니다."),
    EXPIRED_PHONE_CHECK_REQUEST(302, "휴대폰 검증 확인 시간이 지났습니다."),
    EMPTY_VALIDATION_NUMBER(303, "템플릿에 인증번호는 필수입니다."),

    NOT_VERIFIED_PHONE(400, "휴대폰 검증이 선행 되어야 합니다."),
    ;

    private final int code;
    private final String message;
}
