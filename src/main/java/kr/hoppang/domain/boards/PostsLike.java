package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostsLike(
        Long id,
        Long postId,
        Long userId,
        LocalDateTime clickedAt
) { }
