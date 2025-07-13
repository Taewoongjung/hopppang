package kr.hoppang.application.command.boards.event.events;

import lombok.Builder;

@Builder
public record RemovePostsReplyLikeCommandEvent(
        long replyId,
        long unlikedUserId
) { }
