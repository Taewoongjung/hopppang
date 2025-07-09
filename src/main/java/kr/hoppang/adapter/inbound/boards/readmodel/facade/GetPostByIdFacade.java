package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostByIdFacadeResultDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindBoardsByIdQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindBoardsByIdQuery;
import kr.hoppang.application.readmodel.user.handlers.FindUserByIdQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindUserByIdQuery;
import kr.hoppang.domain.boards.Boards;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPostByIdFacade {

    private final PostsQueryRepository postsQueryRepository;
    private final FindUserByIdQueryHandler findUserByIdQueryHandler;
    private final FindBoardsByIdQueryHandler findBoardsByIdQueryHandler;

    public GetPostByIdFacadeResultDto query(final long postId) {
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

        return GetPostByIdFacadeResultDto.builder()
                .id(posts.getId())
                .boardName(boards.getName())
                .registerName(user.getName())
                .title(posts.getTitle())
                .contents(posts.getContents())
                .isAnonymous(posts.getIsAnonymous())
                .createdAt(posts.getCreatedAt())
                .lastModified(posts.getLastModified())
                .build();
    }
}
