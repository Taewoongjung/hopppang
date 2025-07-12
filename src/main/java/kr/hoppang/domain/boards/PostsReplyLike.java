package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostsReplyLike(
        Long id,
        Long postReplyId,
        Long userId,
        LocalDateTime clickedAt
) { }
