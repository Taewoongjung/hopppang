package kr.hoppang.adapter.common.exception.custom;

import lombok.Getter;

@Getter
public class HoppangExpiredRefreshToken extends RuntimeException {

    private final int code;
    private final String redirectUrl;

    public HoppangExpiredRefreshToken(final int code) {
        super("토큰이 만료되었습니다. 다시 로그인 부탁드려요.");

        this.code = code;
        this.redirectUrl = "/mypage";
    }
}
