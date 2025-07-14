package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostByIdFacadeResultDto;
import kr.hoppang.application.command.boards.event.events.AddPostsViewCommandEvent;
import kr.hoppang.application.readmodel.boards.hanlders.FindBoardsByIdQueryHandler;
import kr.hoppang.application.readmodel.boards.hanlders.FindPostsViewsByIdsQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindBoardsByIdQuery;
import kr.hoppang.application.readmodel.boards.queries.FindPostsViewsByIdsQuery;
import kr.hoppang.application.readmodel.user.handlers.FindUserByIdQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindUserByIdQuery;
import kr.hoppang.domain.boards.Boards;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPostByIdFacade {

    private final ApplicationEventPublisher eventPublisher;
    private final PostsQueryRepository postsQueryRepository;
    private final FindUserByIdQueryHandler findUserByIdQueryHandler;
    private final FindBoardsByIdQueryHandler findBoardsByIdQueryHandler;
    private final FindPostsViewsByIdsQueryHandler findPostsViewsByIdsQueryHandler;


    public GetPostByIdFacadeResultDto query(
            final long postId,
            final Long loggedInUserId
    ) {
        Posts posts = postsQueryRepository.findPostsByPostId(postId);

        Boards boards = findBoardsByIdQueryHandler.handle(
                FindBoardsByIdQuery.builder()
                        .boardId(posts.getBoardId())
                        .build()
        );

        User user = findUserByIdQueryHandler.handle(
                FindUserByIdQuery.builder()
                        .userId(posts.getRegisterId())
                        .build()
        );

        Map<Long, Long> postViewCount = findPostsViewsByIdsQueryHandler.handle(
                FindPostsViewsByIdsQuery.builder()
                        .postIds(
                                List.of(postId)
                        ).build()
        ).viewCountDatas();

        eventPublisher.publishEvent(
                AddPostsViewCommandEvent.builder()
                        .postId(postId)
                        .clickedAt(LocalDateTime.now())
                        .userId(loggedInUserId)
                        .build()
        );

        return GetPostByIdFacadeResultDto.builder()
                .id(posts.getId())
                .boardName(boards.getName())
                .registerName(user.getName())
                .registerId(user.getId())
                .title(posts.getTitle())
                .contents(posts.getContents())
                .isAnonymous(posts.getIsAnonymous())
                .createdAt(posts.getCreatedAt())
                .lastModified(posts.getLastModified())
                .viewCount(postViewCount.get(postId) != null ? postViewCount.get(postId) : 0L)
                .build();
    }
}
