package kr.hoppang.adapter.inbound.boards.command;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import kr.hoppang.adapter.inbound.boards.webdto.AddPostRepliesWebDtoV1;
import kr.hoppang.adapter.inbound.boards.webdto.AddPostWebDtoV1;
import kr.hoppang.adapter.inbound.user.AuthenticationUserId;
import kr.hoppang.application.command.boards.commands.AddPostReplyCommand;
import kr.hoppang.application.command.boards.commands.AddPostsCommand;
import kr.hoppang.application.command.boards.event.events.AddPostsLikeCommandEvent;
import kr.hoppang.application.command.boards.event.events.AddPostsReplyLikeCommandEvent;
import kr.hoppang.application.command.boards.handlers.AddPostReplyCommandHandler;
import kr.hoppang.application.command.boards.handlers.AddPostsCommandHandler;
import kr.hoppang.application.command.boards.event.events.RemovePostsLikeCommandEvent;
import kr.hoppang.application.command.boards.event.events.RemovePostsReplyLikeCommandEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/boards")
public class BoardCommandController {

    private final ApplicationEventPublisher eventPublisher;
    private final AddPostsCommandHandler addPostsCommandHandler;
    private final AddPostReplyCommandHandler addPostReplyCommandHandler;


    @PostMapping("/posts")
    public ResponseEntity<AddPostWebDtoV1.Res> addPost(
            @RequestBody AddPostWebDtoV1.Req req,
            @AuthenticationUserId final Long registerId
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        AddPostWebDtoV1.Res.builder()
                                .createdPostId(
                                        addPostsCommandHandler.handle(
                                                AddPostsCommand.builder()
                                                        .boardId(req.boardId())
                                                        .title(req.title())
                                                        .contents(req.contents())
                                                        .isAnonymous(req.isAnonymous())
                                                        .registerId(registerId)
                                                        .build()
                                        )
                                )
                                .build()
                );
    }

    @PostMapping("/posts/{postId}/replies")
    public ResponseEntity<AddPostRepliesWebDtoV1.Res> addPostReplies(
            @PathVariable(value = "postId") final long postId,
            @Valid @RequestBody final AddPostRepliesWebDtoV1.Req req,
            @AuthenticationUserId final Long registerId
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        AddPostRepliesWebDtoV1.Res.builder()
                                .createdReplyId(
                                        addPostReplyCommandHandler.handle(
                                                AddPostReplyCommand.builder()
                                                        .postId(postId)
                                                        .rootReplyId(req.rootReplyId())
                                                        .contents(req.contents())
                                                        .registerId(registerId)
                                                        .build()
                                        )
                                )
                                .build()
                );
    }

    @PatchMapping("/posts/{postId}/likes")
    public void addPostLike(
            @PathVariable(value = "postId") final long postId,
            @AuthenticationUserId final long likedUserId
    ) {

        eventPublisher.publishEvent(
                AddPostsLikeCommandEvent.builder()
                        .postId(postId)
                        .likedUserId(likedUserId)
                        .clickedAt(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/posts/{postId}/likes")
    public void removePostLike(
            @PathVariable(value = "postId") final long postId,
            @AuthenticationUserId final long unlikedUserId
    ) {

        eventPublisher.publishEvent(
                RemovePostsLikeCommandEvent.builder()
                        .postId(postId)
                        .unlikedUserId(unlikedUserId)
                        .build()
        );
    }

    @PatchMapping("/posts/replies/{replyId}/likes")
    public void addPostReplyLike(
            @PathVariable(value = "replyId") final long replyId,
            @AuthenticationUserId final long likedUserId
    ) {

        eventPublisher.publishEvent(
                AddPostsReplyLikeCommandEvent.builder()
                        .replyId(replyId)
                        .likedUserId(likedUserId)
                        .clickedAt(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/posts/replies/{replyId}/likes")
    public void removePostReplyLike(
            @PathVariable(value = "replyId") final long replyId,
            @AuthenticationUserId final long unlikedUserId
    ) {

        eventPublisher.publishEvent(
                RemovePostsReplyLikeCommandEvent.builder()
                        .replyId(replyId)
                        .unlikedUserId(unlikedUserId)
                        .build()
        );
    }
}