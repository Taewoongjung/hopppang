package kr.hoppang.adapter.inbound.boards.webdto;

public record AddPostWebDtoV1() {

    public record Req(
            long boardId,
            String title,
            String contents,
            boolean isAnonymous
    ) { }
}
