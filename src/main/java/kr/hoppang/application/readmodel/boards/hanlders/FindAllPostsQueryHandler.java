package kr.hoppang.application.readmodel.boards.hanlders;

import java.util.List;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindAllPostsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindAllPostsQueryResult;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import kr.hoppang.domain.boards.repository.dto.ConditionOfFindPosts;
import kr.hoppang.util.CountQueryExecutionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindAllPostsQueryHandler implements IQueryHandler<FindAllPostsQuery, FindAllPostsQueryResult> {

    private final PostsQueryRepository postsQueryRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    public FindAllPostsQueryResult handle(final FindAllPostsQuery query) {

        ConditionOfFindPosts condition = ConditionOfFindPosts.builder()
                .boardIds(query.boardIds())
                .limit(query.limit())
                .offset(query.offset())
                .build();

        List<Posts> postsList = postsQueryRepository.findAllPostsByCondition(condition);

        long count = CountQueryExecutionUtil.count(
                postsList,
                query.offset(),
                query.limit(),
                () -> postsQueryRepository.countAllPostsByCondition(condition)
        );

        return FindAllPostsQueryResult.builder()
                .count(count)
                .postsList(postsList)
                .build();
    }
}
