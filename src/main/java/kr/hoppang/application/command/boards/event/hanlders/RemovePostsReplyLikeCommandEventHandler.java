package kr.hoppang.application.command.boards.event.hanlders;

import kr.hoppang.application.command.boards.event.events.RemovePostsReplyLikeCommandEvent;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemovePostsReplyLikeCommandEventHandler {

    private final PostsReplyLikeCommandRepository postsReplyLikeCommandRepository;


    @Async
    @EventListener
    public void handle(final RemovePostsReplyLikeCommandEvent command) {

        postsReplyLikeCommandRepository.delete(
                PostsReplyLike.builder()
                        .postReplyId(command.replyId())
                        .userId(command.unlikedUserId())
                        .build()
        );
    }
}