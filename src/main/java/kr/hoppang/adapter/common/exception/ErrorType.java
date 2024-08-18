package kr.hoppang.adapter.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // 도메인 depth 순서로 작성

    INVALID_USER_INFO(10, "유저 정보가 정확하지 않습니다."),
    LOGIN_FAIL(100, "로그인 실패, 아이디 또는 비밀번호를 확인해주세요."),
    EXPIRED_ACCESS_TOKEN(101, "만료 된 토큰입니다.");

    private final int code;
    private final String message;
}
