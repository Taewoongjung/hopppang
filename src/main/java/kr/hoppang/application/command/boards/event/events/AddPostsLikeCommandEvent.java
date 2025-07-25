package kr.hoppang.application.command.boards.event.events;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AddPostsLikeCommandEvent(
        long postId,
        long likedUserId,
        LocalDateTime clickedAt
) { }
