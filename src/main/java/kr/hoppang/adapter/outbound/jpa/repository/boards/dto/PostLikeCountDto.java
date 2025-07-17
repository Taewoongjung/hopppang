package kr.hoppang.adapter.outbound.jpa.repository.boards.dto;

public record PostLikeCountDto(
        Long postId,
        Long viewCount
) { }
