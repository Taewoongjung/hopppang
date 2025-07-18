package kr.hoppang.application.command.boards.event.hanlders;

import java.time.LocalDateTime;
import kr.hoppang.application.command.boards.event.events.AddPostsBookmarkCommandEvent;
import kr.hoppang.domain.boards.PostsBookmark;
import kr.hoppang.domain.boards.repository.PostsBookmarkCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddPostsBookmarkCommandEventHandler {

    private final PostsBookmarkCommandRepository postsBookmarkCommandRepository;


    @Async
    @EventListener
    public void handle(final AddPostsBookmarkCommandEvent event) {
        postsBookmarkCommandRepository.create(
                PostsBookmark.builder()
                        .postId(event.postId())
                        .userId(event.userId())
                        .clickedAt(LocalDateTime.now())
                        .build()
        );
    }
}
