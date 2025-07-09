package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeResultDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindAllPostsQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindAllPostsQuery;
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

        List<User> authorList = userRepository.findAllUsersIdIn(
                result.postsList().stream()
                        .map(Posts::getRegisterId)
                        .distinct()
                        .toList()
        );

        return GetPostsByConditionFacadeResultDto.of(
                result.count(),
                result.postsList(),
                authorList
        );
    }
}
