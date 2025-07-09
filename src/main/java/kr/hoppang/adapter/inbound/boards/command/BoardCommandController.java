package kr.hoppang.adapter.inbound.boards.command;

import kr.hoppang.adapter.inbound.boards.webdto.AddPostWebDtoV1;
import kr.hoppang.adapter.inbound.user.AuthenticationUserId;
import kr.hoppang.application.command.boards.commands.AddPostsCommand;
import kr.hoppang.application.command.boards.handlers.AddPostsCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
