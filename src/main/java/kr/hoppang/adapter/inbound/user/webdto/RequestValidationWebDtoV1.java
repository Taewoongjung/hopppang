package kr.hoppang.adapter.inbound.user.webdto;

public class RequestValidationWebDtoV1 {

    public record PhoneValidationReq(String email,
                                     String targetPhoneNumber,

                                     ValidationType validationType) {

    }
}
