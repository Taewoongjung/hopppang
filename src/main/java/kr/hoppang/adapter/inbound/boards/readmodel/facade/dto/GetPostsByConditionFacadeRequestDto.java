package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record GetPostsByConditionFacadeRequestDto(
        int limit,
        long offset,
        String searchWord,
        List<Long> boardIds,
        Long userId,
        Boolean bookmarkOnly,
        Boolean repliesOnly
) { }