package kr.hoppang.adapter.inbound.user.webdto;

public class SsoSignUpWebDtoV1 {

    public record Req(String deviceId,
                      String userPhoneNumber,
                      String address,
                      String subAddress,
                      String buildingNumber,
                      Boolean isPushOn) {

    }
}
