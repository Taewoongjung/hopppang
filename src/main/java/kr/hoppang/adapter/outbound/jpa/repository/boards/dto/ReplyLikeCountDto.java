package kr.hoppang.adapter.outbound.jpa.repository.boards.dto;

public record ReplyLikeCountDto(
        Long postReplyId,
        Long count
) { }
