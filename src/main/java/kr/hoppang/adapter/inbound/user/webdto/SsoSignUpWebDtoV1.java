package kr.hoppang.adapter.inbound.user.webdto;

import kr.hoppang.domain.user.OauthType;

public class SsoSignUpWebDtoV1 {

    public record Req(String deviceId,
                      String deviceType,
                      String tokenInfo
    ) {

    }

    public record Res(boolean isSuccess,
                      boolean isTheFirstLogIn,
                      String userEmail,
                      OauthType oauthType) {


    }
}
