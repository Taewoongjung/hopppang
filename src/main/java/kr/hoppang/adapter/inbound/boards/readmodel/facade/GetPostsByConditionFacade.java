package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostsByConditionFacadeResultDto;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import kr.hoppang.domain.boards.repository.dto.ConditionOfFindPosts;
import kr.hoppang.util.CountQueryExecutionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPostsByConditionFacade {

    private final PostsQueryRepository postsQueryRepository;


    public GetPostsByConditionFacadeResultDto query(
            int limit,
            long offset,
            List<Long> boardIds
    ) {

        ConditionOfFindPosts condition = ConditionOfFindPosts.builder()
                .boardIds(boardIds)
                .limit(limit)
                .offset(offset)
                .build();

        List<Posts> postsList = postsQueryRepository.findAllPostsByCondition(condition);

        long count = CountQueryExecutionUtil.count(
                postsList,
                offset,
                limit,
                () -> postsQueryRepository.countAllPostsByCondition(condition)
        );

        return GetPostsByConditionFacadeResultDto.builder()
                .postsList(postsList)
                .count(count)
                .build();
    }
}
