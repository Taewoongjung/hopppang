package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeRequestDto;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeResultDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindAllPostsQueryHandler;
import kr.hoppang.application.readmodel.boards.hanlders.FindPostsCountOfLikesByIdsQueryHandler;
import kr.hoppang.application.readmodel.boards.hanlders.FindPostsViewsByIdsQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindAllPostsQuery;
import kr.hoppang.application.readmodel.boards.queries.FindPostsCountOfLikesByIdsQuery;
import kr.hoppang.application.readmodel.boards.queries.FindPostsViewsByIdsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindAllPostsQueryResult;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsBookmarkQueryRepository;
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPostsByConditionFacade {

    private final UserRepository userRepository;
    private final FindAllPostsQueryHandler findAllPostsByCondition;
    private final PostsReplyQueryRepository postsReplyQueryRepository;
    private final PostsBookmarkQueryRepository postsBookmarkQueryRepository;
    private final FindPostsViewsByIdsQueryHandler findPostsViewsByIdsQueryHandler;
    private final FindPostsCountOfLikesByIdsQueryHandler findPostsCountOfLikesByIdsQueryHandler;


    public GetPostsByConditionFacadeResultDto query(
            final GetPostsByConditionFacadeRequestDto requestDto
    ) {

        FindAllPostsQueryResult result = findAllPostsByCondition.handle(
                FindAllPostsQuery.builder()
                        .boardIds(requestDto.boardIds())
                        .userId(requestDto.userId())
                        .searchWord(requestDto.searchWord())
                        .bookmarkOnly(requestDto.bookmarkOnly())
                        .repliesOnly(requestDto.repliesOnly())
                        .limit(requestDto.limit())
                        .offset(requestDto.offset())
                        .build()
        );

        if (result.postsList().isEmpty()) {
            return GetPostsByConditionFacadeResultDto.of(
                    result.count(),
                    result.postsList(),
                    Collections.emptyList(),
                    Collections.emptyMap(),
                    Collections.emptyMap(),
                    Collections.emptyMap(),
                    null
            );
        }

        List<User> authorList = userRepository.findAllUsersIdIn(
                result.postsList().stream()
                        .map(Posts::getRegisterId)
                        .distinct()
                        .toList()
        );

        List<Long> postIds = result.postsList().stream()
                .map(Posts::getId)
                .toList();

        Map<Long, Long> viewCountByPostId = findPostsViewsByIdsQueryHandler.handle(
                FindPostsViewsByIdsQuery.builder()
                        .postIds(postIds)
                        .build()
        ).viewCountDatas();

        Map<Long, Long> postLikeCountByPostId = findPostsCountOfLikesByIdsQueryHandler.handle(
                FindPostsCountOfLikesByIdsQuery.builder()
                        .postIds(postIds)
                        .build()
        ).countDatas();

        Map<Long, Long> replyCountByPostId = postsReplyQueryRepository.findCountOfLikesByPostId(
                postIds);

        Map<Long, Boolean> isBookmarkedGroupByPostId = postsBookmarkQueryRepository.isBookmarkedGroupByPostsIdAndUserId(
                postIds, requestDto.userId());

        return GetPostsByConditionFacadeResultDto.of(
                result.count(),
                result.postsList(),
                authorList,
                viewCountByPostId,
                replyCountByPostId,
                postLikeCountByPostId,
                isBookmarkedGroupByPostId
        );
    }
}
