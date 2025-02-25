package kr.hoppang.adapter.inbound.chassis.admin.webdto;

import lombok.Builder;

public record GetAllEstimationsProvidedToCustomersWebDtoV1() {

    @Builder
    public record Res(
            long count
    ) { }
}
