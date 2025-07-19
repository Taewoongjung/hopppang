package kr.hoppang.domain.boards.repository.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ConditionOfFindPosts(
        List<Long> boardIds,
        Long userId,
        String searchWord,
        Boolean bookmarkOnly,
        int limit,
        long offset
) { }
