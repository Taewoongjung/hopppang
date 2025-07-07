package kr.hoppang.adapter.inbound.boards.readmodel;

import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetAllBoardsWebDtoV1;
import kr.hoppang.application.readmodel.boards.hanlders.FindBoardsQueryHandler;
import kr.hoppang.application.util.EmptyQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/boards")
public class BoardQueryController {

    private final FindBoardsQueryHandler findBoardsQueryHandler;


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
}
