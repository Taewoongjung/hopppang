package kr.hoppang.adapter.inbound.boards.webdto;

import lombok.Builder;

public record AddPostWebDtoV1() {

    public record Req(
            long boardId,
            String title,
            String contents,
            boolean isAnonymous
    ) { }

    @Builder
    public record Res(
            long createdPostId
    ) { }
}
