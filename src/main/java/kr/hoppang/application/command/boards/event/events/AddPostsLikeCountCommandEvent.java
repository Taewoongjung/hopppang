package kr.hoppang.application.command.boards.event.events;

import java.util.Map;
import lombok.Builder;

@Builder
public record AddPostsLikeCountCommandEvent(
        Map<Long, Long> countOfLikesByPostId
) { }
