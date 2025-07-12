package kr.hoppang.application.command.event.events;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AddPostsReplyLikeCommandEvent(
        long replyId,
        long likedUserId,
        LocalDateTime clickedAt
) { }
