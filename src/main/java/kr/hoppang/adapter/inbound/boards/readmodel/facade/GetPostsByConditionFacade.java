package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeResultDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindAllPostsQueryHandler;
import kr.hoppang.application.readmodel.boards.hanlders.FindPostsViewsByIdsQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindAllPostsQuery;
import kr.hoppang.application.readmodel.boards.queries.FindPostsViewsByIdsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindAllPostsQueryResult;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPostsByConditionFacade {

    private final UserRepository userRepository;
    private final FindAllPostsQueryHandler findAllPostsByCondition;
    private final FindPostsViewsByIdsQueryHandler findPostsViewsByIdsQueryHandler;

    public GetPostsByConditionFacadeResultDto query(
            int limit,
            long offset,
            List<Long> boardIds
    ) {

        FindAllPostsQueryResult result = findAllPostsByCondition.handle(
                FindAllPostsQuery.builder()
                        .boardIds(boardIds)
                        .limit(limit)
                        .offset(offset)
                        .build()
        );

        if (result.postsList().isEmpty()) {
            return GetPostsByConditionFacadeResultDto.of(
                    result.count(),
                    result.postsList(),
                    Collections.emptyList(),
                    Collections.emptyMap()
            );
        }

        List<User> authorList = userRepository.findAllUsersIdIn(
                result.postsList().stream()
                        .map(Posts::getRegisterId)
                        .distinct()
                        .toList()
        );

        Map<Long, Long> viewCountByPostId = findPostsViewsByIdsQueryHandler.handle(
                FindPostsViewsByIdsQuery.builder()
                        .postIds(
                                result.postsList().stream()
                                        .map(Posts::getId)
                                        .toList()
                        )
                        .build()
        ).viewCountDatas();

        return GetPostsByConditionFacadeResultDto.of(
                result.count(),
                result.postsList(),
                authorList,
                viewCountByPostId
        );
    }
}
