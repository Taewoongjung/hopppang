package kr.hoppang.adapter.inbound.chassis.webdto;

import lombok.NonNull;

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
