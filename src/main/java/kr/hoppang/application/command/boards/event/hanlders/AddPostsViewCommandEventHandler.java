package kr.hoppang.application.command.boards.event.hanlders;

import kr.hoppang.application.command.boards.event.events.AddPostsViewCommandEvent;
import kr.hoppang.domain.boards.PostsView;
import kr.hoppang.domain.boards.repository.PostsViewCommandRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AddPostsViewCommandEventHandler {

    private final PostsViewCommandRepository postsViewCommandRepository;

    public AddPostsViewCommandEventHandler(
            @Qualifier(value = "PostsViewCommandRepositoryRedis") final PostsViewCommandRepository postsViewCommandRepository
    ) {
        this.postsViewCommandRepository = postsViewCommandRepository;
    }

    @Async
    @EventListener
    public void handle(final AddPostsViewCommandEvent event) {
        postsViewCommandRepository.create(
                PostsView.builder()
                        .postId(event.postId())
                        .clickedAt(event.clickedAt())
                        .userId(event.userId())
                        .build()
        );
    }
}
