package kr.hoppang.adapter.inbound.boards.readmodel;

import jakarta.validation.Valid;
import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostByIdFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostsByConditionFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeResultDto;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetAllBoardsWebDtoV1;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetPostsByConditionWebDtoV1;
import kr.hoppang.application.readmodel.boards.hanlders.FindBoardsQueryHandler;
import kr.hoppang.application.util.EmptyQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/boards")
public class BoardQueryController {

    private final GetPostByIdFacade getPostByIdFacade;
    private final FindBoardsQueryHandler findBoardsQueryHandler;
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
                                .postsList(resultDto.postsList())
                                .count(resultDto.count())
                                .build()
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
}
