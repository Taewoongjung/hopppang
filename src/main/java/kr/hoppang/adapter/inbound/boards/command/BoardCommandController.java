package kr.hoppang.adapter.inbound.boards.command;

import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.boards.webdto.AddPostRepliesWebDtoV1;
import kr.hoppang.adapter.inbound.boards.webdto.AddPostWebDtoV1;
import kr.hoppang.adapter.inbound.user.AuthenticationUserId;
import kr.hoppang.application.command.boards.commands.AddPostReplyCommand;
import kr.hoppang.application.command.boards.commands.AddPostsCommand;
import kr.hoppang.application.command.boards.handlers.AddPostReplyCommandHandler;
import kr.hoppang.application.command.boards.handlers.AddPostsCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
