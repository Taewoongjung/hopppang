package kr.hoppang.application.command.boards.event.events;

import lombok.Builder;

@Builder
public record RemovePostsBookmarkCommandEvent(
        long postId,
        long userId
) { }
