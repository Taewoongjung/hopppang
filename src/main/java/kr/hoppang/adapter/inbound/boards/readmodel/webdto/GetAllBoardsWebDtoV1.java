package kr.hoppang.adapter.inbound.boards.readmodel.webdto;

import lombok.Builder;

public record GetAllBoardsWebDtoV1() {

    @Builder
    public record Res(
            long id,
            String name
    ) { }
}
