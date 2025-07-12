package kr.hoppang.application.command.event.hanlders;

import kr.hoppang.application.command.event.events.AddPostsLikeCommandEvent;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AddPostsLikeCommandEventHandler {

    private final PostsLikeCommandRepository postsLikeCommandRepository;


    @Async
    @TransactionalEventListener
    public void handle(final AddPostsLikeCommandEvent command) {

        postsLikeCommandRepository.create(
                PostsLike.builder()
                        .postId(command.postId())
                        .userId(command.likedUserId())
                        .clickedAt(command.clickedAt())
                        .build()
        );
    }
}
