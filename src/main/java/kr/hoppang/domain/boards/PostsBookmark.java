package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostsBookmark(
        Long postId,
        Long userId,
        LocalDateTime clickedAt
) { }