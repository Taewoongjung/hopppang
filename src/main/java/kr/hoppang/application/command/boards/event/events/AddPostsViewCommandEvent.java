package kr.hoppang.application.command.boards.event.events;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AddPostsViewCommandEvent(
        Long postId,
        LocalDateTime clickedAt,
        Long userId
) { }
