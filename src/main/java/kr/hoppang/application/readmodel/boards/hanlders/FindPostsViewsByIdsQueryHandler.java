package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindPostsViewsByIdsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindPostsViewByIdsQueryResult;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsViewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPostsViewsByIdsQueryHandler implements IQueryHandler<FindPostsViewsByIdsQuery, FindPostsViewByIdsQueryResult> {

    private final List<PostsViewQueryRepository> postsViewQueryRepositoryList;
    private EnumMap<BoardsRepositoryStrategy, PostsViewQueryRepository> postsViewQueryRepositoryEnumMap;

    @PostConstruct
    void init() {
        postsViewQueryRepositoryEnumMap = postsViewQueryRepositoryList.stream()
                .collect(Collectors.toMap(
                        PostsViewQueryRepository::strategy,
                        (postsViewQueryRepository) -> postsViewQueryRepository,
                        (existRepository, newRepository) -> existRepository,
                        () -> new EnumMap<>(BoardsRepositoryStrategy.class)
                ));
    }

    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    public FindPostsViewByIdsQueryResult handle(final FindPostsViewsByIdsQuery query) {
        Map<Long, Long> dataFromCache = postsViewQueryRepositoryEnumMap.get(
                BoardsRepositoryStrategy.CACHE
        ).findCountOfViewsByPostIds(query.postIds());

        if (dataFromCache == null) {
            dataFromCache = postsViewQueryRepositoryEnumMap.get(
                    BoardsRepositoryStrategy.RDB
            ).findCountOfViewsByPostIds(query.postIds());
        }

        return FindPostsViewByIdsQueryResult.builder()
                .viewCountDatas(dataFromCache)
                .build();
    }
}
