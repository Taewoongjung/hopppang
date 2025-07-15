package kr.hoppang.application.command.boards.event.hanlders;

import java.util.Set;
import kr.hoppang.application.command.boards.event.events.AddPostsViewCountCommandForCachingEvent;
import kr.hoppang.domain.boards.PostsView;
import kr.hoppang.domain.boards.repository.PostsViewCommandRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AddPostsViewCountCommandForCachingEventHandler {

    private final PostsViewCommandRepository postsViewCommandRepository;

    public AddPostsViewCountCommandForCachingEventHandler(
            @Qualifier("PostsViewCommandRepositoryRedis") final PostsViewCommandRepository postsViewCommandRepository
    ) {
        this.postsViewCommandRepository = postsViewCommandRepository;
    }

    @Async
    @EventListener
    public void handle(final AddPostsViewCountCommandForCachingEvent event) {
        Set<Long> keySet = event.countDataForCaching().keySet();

        postsViewCommandRepository.createAll(
                keySet.stream()
                        .map(
                                e -> PostsView.builder()
                                        .postId(e)
                                        .originCount(event.countDataForCaching().get(e))
                                        .build()
                        )
                        .toList()
        );
    }
}
