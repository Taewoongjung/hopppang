package kr.hoppang.adapter.common.exception.custom;

import kr.hoppang.domain.user.OauthType;
import lombok.Getter;

@Getter
public class HoppangDuplicatedLoginException extends RuntimeException {

    private final int code;
    private final String email;
    private final String oauthType;

    public HoppangDuplicatedLoginException(final String email, final OauthType oauthType) {
        super("기존 호빵 계정에 연동된 소셜 계정이 있습니다. 해당 소셜로 로그인 해주세요.");

        this.code = 1;
        this.email = email;
        this.oauthType = oauthType.getType();
    }
}
