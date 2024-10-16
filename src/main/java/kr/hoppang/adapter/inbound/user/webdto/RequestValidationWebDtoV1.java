package kr.hoppang.adapter.inbound.user.webdto;

public class RequestValidationWebDtoV1 {

    public record PhoneValidationReq(String targetPhoneNumber, ValidationType validationType) {

    }
}
