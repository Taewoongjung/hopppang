package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetRecentPostsFacadeResultDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindBoardsQueryHandler;
import kr.hoppang.application.readmodel.boards.hanlders.FindPostsViewsByIdsQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindPostsViewsByIdsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindBoardsQueryResult;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRecentPostsFacade {

    private final PostsQueryRepository postsQueryRepository;
    private final FindBoardsQueryHandler findBoardsQueryHandler;
    private final FindPostsViewsByIdsQueryHandler findPostsViewsByIdsQueryHandler;


    @Cacheable(
            value = "post",
            key = "'recents'",
            cacheManager = "cacheManagerForTtl5Minutes"
    )
    public List<GetRecentPostsFacadeResultDto> query() {

        List<FindBoardsQueryResult> boardsQueryResults = findBoardsQueryHandler.handle(
                EmptyQuery.of()
        );

        List<Posts> recentPosts = postsQueryRepository.findRecentFivePosts();

        Map<Long, Long> viewCountOfEachPosts = findPostsViewsByIdsQueryHandler.handle(
                FindPostsViewsByIdsQuery.builder()
                        .postIds(
                                recentPosts.stream()
                                        .map(Posts::getId)
                                        .toList()
                        )
                        .build()
        ).viewCountDatas();

        return recentPosts.stream()
                .map(post -> {
                    Optional<FindBoardsQueryResult> boardInfo = boardsQueryResults.stream()
                            .filter(board ->
                                    post.getBoardId().equals(board.id())
                            ).findFirst();

                    return GetRecentPostsFacadeResultDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .contents(post.getContents())
                            .boardName(
                                    boardInfo.map(FindBoardsQueryResult::name)
                                            .orElse(null)
                            )
                            .createdTime(getCreatedTime(post.getCreatedAt()))
                            .viewCount(
                                    viewCountOfEachPosts.get(post.getBoardId()) != null ?
                                            viewCountOfEachPosts.get(post.getBoardId()) : 0
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String getCreatedTime(final LocalDateTime createdAt) {
        return createdAt.getHour() + ":" + createdAt.getMinute();
    }
}
