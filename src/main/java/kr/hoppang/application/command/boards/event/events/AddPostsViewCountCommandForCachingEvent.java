package kr.hoppang.application.command.boards.event.events;

import java.util.Map;
import lombok.Builder;

@Builder
public record AddPostsViewCountCommandForCachingEvent(
        Map<Long, Long> countDataForCaching
) { }
