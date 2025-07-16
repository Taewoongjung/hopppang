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
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRecentPostsFacade {

    private final PostsQueryRepository postsQueryRepository;
    private final FindBoardsQueryHandler findBoardsQueryHandler;
    private final PostsReplyQueryRepository postsReplyQueryRepository;
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

        List<Long> postIds = recentPosts.stream()
                .map(Posts::getId)
                .toList();

        Map<Long, Long> viewCountOfEachPosts = findPostsViewsByIdsQueryHandler.handle(
                FindPostsViewsByIdsQuery.builder()
                        .postIds(postIds)
                        .build()
        ).viewCountDatas();

        Map<Long, Long> replyCountOfEachPosts = postsReplyQueryRepository.findCountOfLikesByPostId(
                postIds
        );

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
                                    viewCountOfEachPosts.get(post.getId()) != null ?
                                            viewCountOfEachPosts.get(post.getId()) : 0
                            )
                            .replyCount(
                                    replyCountOfEachPosts.get(post.getId()) != null ?
                                            replyCountOfEachPosts.get(post.getId()) : 0
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String getCreatedTime(final LocalDateTime createdAt) {
        return createdAt.getHour() + ":" + createdAt.getMinute();
    }
}
