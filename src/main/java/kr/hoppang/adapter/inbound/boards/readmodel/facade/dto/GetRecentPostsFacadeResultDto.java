package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import lombok.Builder;

@Builder
public record GetRecentPostsFacadeResultDto(
        long id,
        String title,
        String contents,
        String boardName,
        String createdTime, // 00:00
        long viewCount,
        long replyCount
) { }