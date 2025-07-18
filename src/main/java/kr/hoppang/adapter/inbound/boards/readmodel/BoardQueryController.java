package kr.hoppang.adapter.inbound.boards.readmodel;

import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostByIdFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostRepliesByIdFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetPostsByConditionFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.GetRecentPostsFacade;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostByIdFacadeResultDto;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostRepliesByIdFacadeResultDto;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostRepliesByIdFacadeResultDto.PostsRootReplyFacadeDto;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeResultDto;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetRecentPostsFacadeResultDto;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetAllBoardsWebDtoV1;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetPostsByConditionWebDtoV1;
import kr.hoppang.adapter.inbound.boards.readmodel.webdto.GetPostsByConditionWebDtoV1.Res.PostWebDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindBoardsQueryHandler;
import kr.hoppang.application.readmodel.boards.queryresults.FindBoardsQueryResult;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.domain.boards.PostsReplyOrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/boards")
public class BoardQueryController {

    private final GetPostByIdFacade getPostByIdFacade;
    private final GetRecentPostsFacade getRecentPostsFacade;
    private final FindBoardsQueryHandler findBoardsQueryHandler;
    private final GetPostRepliesByIdFacade getPostRepliesByIdFacade;
    private final GetPostsByConditionFacade getPostsByConditionFacade;


    @GetMapping("")
    public ResponseEntity<List<GetAllBoardsWebDtoV1.Res>> getAllBoards() {

        List<FindBoardsQueryResult> result = findBoardsQueryHandler.handle(
                EmptyQuery.builder().build());

        List<FindBoardsQueryResult> roots = result.stream().filter(f -> f.rootId() == null)
                .toList();

        List<FindBoardsQueryResult> branches = result.stream().filter(f -> f.rootId() != null)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(GetAllBoardsWebDtoV1.Res.of(roots, branches));
    }

    @GetMapping("/posts")
    public ResponseEntity<GetPostsByConditionWebDtoV1.Res> getPostsByCondition(
            @Valid final GetPostsByConditionWebDtoV1.Req req
    ) {

        GetPostsByConditionFacadeResultDto resultDto = getPostsByConditionFacade.query(
                req.limit(),
                req.offset(),
                req.searchWord(),
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
                                                                .createdAt(post.createdAt())
                                                                .viewCount(post.viewCount())
                                                                .likeCount(post.likeCount())
                                                                .replyCount(post.replyCount())
                                                                .build()
                                                )
                                                .toList()
                                )
                                .build()
                );
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<GetPostByIdFacadeResultDto> getPostById(
            @PathVariable final long postId,
            @RequestParam(value = "loggedInUserId", required = false) final Long loggedInUserId
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        getPostByIdFacade.query(postId, loggedInUserId)
                );
    }

    @GetMapping("/posts/{postId}/replies")
    public ResponseEntity<GetPostRepliesByIdFacadeResultDto> getPostReplies(
            @PathVariable(value = "postId") final long postId,
            @RequestParam(value = "loggedInUserId", required = false) final Long loggedInUserId,
            @RequestParam(value = "orderType", required = false) final PostsReplyOrderType orderType
    ) {

        GetPostRepliesByIdFacadeResultDto result = getPostRepliesByIdFacade.query(postId,
                loggedInUserId);

        if (orderType != null) {
            if (PostsReplyOrderType.LIKE_DESC.equals(orderType)) {
                result = new GetPostRepliesByIdFacadeResultDto(
                        result.postsReplyList().stream()
                                .sorted(
                                        Comparator.comparing(PostsRootReplyFacadeDto::getLikes)
                                                .reversed()
                                )
                                .toList()
                );
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/posts/recent")
    public ResponseEntity<List<GetRecentPostsFacadeResultDto>> getRecentPosts() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(getRecentPostsFacade.query());
    }
}