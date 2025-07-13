package kr.hoppang.application.command.boards.event.events;

import lombok.Builder;

@Builder
public record RemovePostsLikeCommandEvent(
        long postId,
        long unlikedUserId
) { }
