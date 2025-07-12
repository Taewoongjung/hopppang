package kr.hoppang.application.command.event.hanlders;

import kr.hoppang.application.command.event.events.AddPostsReplyLikeCommandEvent;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AddPostsReplyLikeCommandEventHandler {

    private final PostsReplyLikeCommandRepository postsReplyLikeCommandRepository;


    @Async
    @TransactionalEventListener
    public void handle(final AddPostsReplyLikeCommandEvent command) {

        postsReplyLikeCommandRepository.create(
                PostsReplyLike.builder()
                        .postReplyId(command.replyId())
                        .userId(command.likedUserId())
                        .clickedAt(command.clickedAt())
                        .build()
        );
    }
}
