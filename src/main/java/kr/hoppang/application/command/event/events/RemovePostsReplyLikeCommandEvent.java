package kr.hoppang.application.command.event.events;

import lombok.Builder;

@Builder
public record RemovePostsReplyLikeCommandEvent(
        long replyId,
        long unlikedUserId
) { }
