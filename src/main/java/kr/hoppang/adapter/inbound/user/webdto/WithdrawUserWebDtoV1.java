package kr.hoppang.adapter.inbound.user.webdto;

import kr.hoppang.domain.user.OauthType;

public class WithdrawUserWebDtoV1 {

    public record Req(OauthType oauthType) {

    }

}
