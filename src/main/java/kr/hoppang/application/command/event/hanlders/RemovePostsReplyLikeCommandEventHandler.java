package kr.hoppang.application.command.event.hanlders;

import kr.hoppang.application.command.event.events.RemovePostsReplyLikeCommandEvent;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RemovePostsReplyLikeCommandEventHandler {

    private final PostsReplyLikeCommandRepository postsReplyLikeCommandRepository;


    @Async
    @TransactionalEventListener
    public void handle(final RemovePostsReplyLikeCommandEvent command) {

        postsReplyLikeCommandRepository.delete(
                PostsReplyLike.builder()
                        .postReplyId(command.replyId())
                        .userId(command.unlikedUserId())
                        .build()
        );
    }
}