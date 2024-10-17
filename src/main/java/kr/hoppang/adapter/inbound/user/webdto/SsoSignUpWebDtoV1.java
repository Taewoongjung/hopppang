package kr.hoppang.adapter.inbound.user.webdto;

public class SsoSignUpWebDtoV1 {

    public record Req(String deviceId) {

    }

    public record Res(boolean isSuccess, boolean isTheFirstLogIn, String userEmail) {

    }
}
