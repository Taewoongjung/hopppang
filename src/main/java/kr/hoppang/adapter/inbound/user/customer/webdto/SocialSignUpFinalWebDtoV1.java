package kr.hoppang.adapter.inbound.user.customer.webdto;

public class SocialSignUpFinalWebDtoV1 {

    public record Req(String userEmail,
                      String userPhoneNumber,
                      Boolean isPushOn,
                      Boolean isAlimTalkOn) {

    }

    public record Res(String email) {

    }

}
