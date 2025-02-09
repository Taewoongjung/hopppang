package kr.hoppang.adapter.inbound.chassis.webdto;

public class AddAndReviseChassisPriceInformationWebDtoV1 {

    public record Req(
            int width,
            int height,
            int price
    ) {

    }

    public record Res(
            boolean isSuccess
    ) {

    }
}
