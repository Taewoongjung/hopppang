package kr.hoppang.application.command.boards.event.hanlders;

import kr.hoppang.application.command.boards.event.events.RemovePostsLikeCommandEvent;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemovePostsLikeCommandEventHandler {

    private final PostsLikeCommandRepository postsLikeCommandRepository;


    @Async
    @EventListener
    public void handle(final RemovePostsLikeCommandEvent command) {

        postsLikeCommandRepository.delete(
                PostsLike.builder()
                        .postId(command.postId())
                        .userId(command.unlikedUserId())
                        .build()
        );
    }
}
