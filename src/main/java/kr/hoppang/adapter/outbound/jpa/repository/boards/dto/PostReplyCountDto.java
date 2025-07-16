package kr.hoppang.adapter.outbound.jpa.repository.boards.dto;

public record PostReplyCountDto(
        Long postId,
        Long replyCount
) {

}
