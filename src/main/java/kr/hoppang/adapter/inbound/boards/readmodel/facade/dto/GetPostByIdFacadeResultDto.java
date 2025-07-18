package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import java.time.LocalDateTime;
import kr.hoppang.util.common.BoolType;
import lombok.Builder;

@Builder
public record GetPostByIdFacadeResultDto(
        Long id,
        long boardId,
        String boardName,
        String registerName,
        Long registerId,
        String title,
        String contents,
        BoolType isAnonymous,
        LocalDateTime createdAt,
        LocalDateTime lastModified,
        long viewCount,
        long likeCount,
        boolean didILiked,
        boolean didIBookmarked
) { }
