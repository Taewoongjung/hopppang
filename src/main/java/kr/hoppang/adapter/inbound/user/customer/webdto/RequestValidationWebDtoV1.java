package kr.hoppang.adapter.inbound.user.customer.webdto;

public class RequestValidationWebDtoV1 {

    public record PhoneValidationReq(String email,
                                     String targetPhoneNumber,

                                     ValidationType validationType) {

    }
}
