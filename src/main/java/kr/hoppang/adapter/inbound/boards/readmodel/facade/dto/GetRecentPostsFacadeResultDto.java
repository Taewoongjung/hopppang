package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GetRecentPostsFacadeResultDto(
        long id,
        String title,
        String contents,
        String boardName,
        LocalDateTime createdTime,
        long viewCount,
        long replyCount
) { }