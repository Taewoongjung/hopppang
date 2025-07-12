package kr.hoppang.application.command.event.hanlders;

import kr.hoppang.application.command.event.events.RemovePostsLikeCommandEvent;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RemovePostsLikeCommandEventHandler {

    private final PostsLikeCommandRepository postsLikeCommandRepository;


    @Async
    @TransactionalEventListener
    public void handle(final RemovePostsLikeCommandEvent command) {

        postsLikeCommandRepository.delete(
                PostsLike.builder()
                        .postId(command.postId())
                        .userId(command.unlikedUserId())
                        .build()
        );
    }
}
