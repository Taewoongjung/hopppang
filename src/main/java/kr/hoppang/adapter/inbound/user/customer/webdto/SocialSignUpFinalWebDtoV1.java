package kr.hoppang.adapter.inbound.user.customer.webdto;

public class SocialSignUpFinalWebDtoV1 {

    public record Req(String userEmail,
                      String userPhoneNumber,
                      String address,
                      String subAddress,
                      String buildingNumber,
                      Boolean isPushOn) {

    }

    public record Res(String email) {

    }

}
