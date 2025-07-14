package kr.hoppang.adapter.outbound.jpa.repository.boards.dto;

public record PostViewCountDto(
        Long postId,
        Long viewCount
) { }
