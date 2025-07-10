package kr.hoppang.adapter.inbound.boards.readmodel;

import jakarta.validation.Valid;
import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostByIdFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostRepliesByIdFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostsByConditionFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostRepliesByIdFacadeResultDto;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeResultDto;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetAllBoardsWebDtoV1;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetPostRepliesWebDtoV1;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetPostsByConditionWebDtoV1;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetPostsByConditionWebDtoV1.Res.PostWebDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindBoardsQueryHandler;
import kr.hoppang.application.util.EmptyQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/boards")
public class BoardQueryController {

    private final GetPostByIdFacade getPostByIdFacade;
    private final FindBoardsQueryHandler findBoardsQueryHandler;
    private final GetPostRepliesByIdFacade getPostRepliesByIdFacade;
    private final GetPostsByConditionFacade getPostsByConditionFacade;


    @GetMapping("")
    public ResponseEntity<List<GetAllBoardsWebDtoV1.Res>> getAllBoards() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        findBoardsQueryHandler.handle(EmptyQuery.builder().build()).stream()
                                .map(e ->
                                        GetAllBoardsWebDtoV1.Res.builder()
                                                .id(e.id())
                                                .name(e.name())
                                                .build()
                                ).toList()
                );
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Object> getPostById(
            @PathVariable final long postId
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        getPostByIdFacade.query(postId)
                );
    }

    @GetMapping("/posts")
    public ResponseEntity<GetPostsByConditionWebDtoV1.Res> getPostsByCondition(
            @Valid final GetPostsByConditionWebDtoV1.Req req
    ) {

        GetPostsByConditionFacadeResultDto resultDto = getPostsByConditionFacade.query(
                req.limit(),
                req.offset(),
                req.boardIdList()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetPostsByConditionWebDtoV1.Res.builder()
                                .count(resultDto.count())
                                .postsList(
                                        resultDto.postsList().stream()
                                                .map(post ->
                                                        PostWebDto.builder()
                                                                .id(post.id())
                                                                .boardId(post.boardId())
                                                                .authorName(post.authorName())
                                                                .title(post.title())
                                                                .contents(post.contents())
                                                                .isAnonymous(post.isAnonymous())
                                                                .isRevised(post.isRevised())
                                                                .createdAt(post.createdAt())
                                                                .build()
                                                )
                                                .toList()
                                )
                                .build()
                );
    }

    @GetMapping("/posts/{postId}/replies")
    public ResponseEntity<GetPostRepliesWebDtoV1.Res> getPostReplies(
            @PathVariable(value = "postId") final long postId,
            @RequestParam(value = "loggedInUserId", required = false) final Long loggedInUserId
    ) {

        GetPostRepliesByIdFacadeResultDto resultDto = getPostRepliesByIdFacade.query(
                postId,
                loggedInUserId
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetPostRepliesWebDtoV1.Res.of(resultDto)
                );
    }
}