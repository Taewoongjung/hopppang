package kr.hoppang.application.command.boards.event.events;

import lombok.Builder;

@Builder
public record AddPostsBookmarkCommandEvent(
        long postId,
        long userId
) { }
