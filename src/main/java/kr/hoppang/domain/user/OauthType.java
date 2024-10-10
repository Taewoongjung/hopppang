package kr.hoppang.domain.user;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_OAUTH_TYPE;

import java.util.Arrays;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthType {

    NON("그냥"), // 소셜 로그인 아님 (현재는 관리자만)
    KKO("카카오"),
    APL("애플"),
    GLE("구글");

    private final String type;

    public static OauthType from(final String target) {
        return Arrays.stream(values())
                .filter(f -> target.equals(f.name()))
                .findFirst()
                .orElseThrow(() -> new HoppangLoginException(NOT_EXIST_OAUTH_TYPE));
    }
}
