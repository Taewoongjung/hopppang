package kr.hoppang.adapter.inbound.chassis.admin.webdto;

import lombok.Builder;

public record GetCountAllUsersWebDtoV1() {

    @Builder
    public record Res(Long count) { }
}
