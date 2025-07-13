package kr.hoppang.application.command.boards.event.events;

import java.util.Map;
import lombok.Builder;

@Builder
public record AddPostsReplyLikeCountCommandEvent(
        Map<Long, Long> countOfLikesByReplyId
) { }
