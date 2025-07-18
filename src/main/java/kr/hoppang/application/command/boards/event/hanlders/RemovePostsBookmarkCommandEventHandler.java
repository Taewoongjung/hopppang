package kr.hoppang.application.command.boards.event.hanlders;

import kr.hoppang.application.command.boards.event.events.RemovePostsBookmarkCommandEvent;
import kr.hoppang.domain.boards.PostsBookmark;
import kr.hoppang.domain.boards.repository.PostsBookmarkCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemovePostsBookmarkCommandEventHandler {

    private final PostsBookmarkCommandRepository postsBookmarkCommandRepository;


    @Async
    @EventListener
    public void handle(final RemovePostsBookmarkCommandEvent event) {
        postsBookmarkCommandRepository.delete(
                PostsBookmark.builder()
                        .postId(event.postId())
                        .userId(event.userId())
                        .build()
        );
    }
}
