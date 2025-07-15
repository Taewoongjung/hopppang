package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.command.boards.event.events.AddPostsViewCountCommandForCachingEvent;
import kr.hoppang.application.readmodel.boards.queries.FindPostsViewsByIdsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindPostsViewByIdsQueryResult;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsViewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPostsViewsByIdsQueryHandler implements
        IQueryHandler<FindPostsViewsByIdsQuery, FindPostsViewByIdsQueryResult> {

    private final ApplicationEventPublisher eventPublisher;
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
        Map<Long, Long> result = new HashMap<>();
        Map<Long, Long> dataFromCache = postsViewQueryRepositoryEnumMap.get(
                BoardsRepositoryStrategy.CACHE
        ).findCountOfViewsByPostIds(query.postIds());

        List<Long> yetCachedViewCountData = new ArrayList<>();
        if (dataFromCache != null) {
            Set<Long> cachedPostIds = dataFromCache.keySet();

            yetCachedViewCountData = query.postIds().stream()
                    .filter(f -> !cachedPostIds.contains(f))
                    .toList();

            result.putAll(dataFromCache);
        }

        if (dataFromCache == null || !yetCachedViewCountData.isEmpty()) {
            Map<Long, Long> countDataFromRdb = postsViewQueryRepositoryEnumMap.get(
                    BoardsRepositoryStrategy.RDB
            ).findCountOfViewsByPostIds(yetCachedViewCountData);

            if (countDataFromRdb != null && !countDataFromRdb.isEmpty()) {
                result.putAll(countDataFromRdb);

                eventPublisher.publishEvent(
                        AddPostsViewCountCommandForCachingEvent.builder()
                                .countDataForCaching(countDataFromRdb)
                                .build()
                );
            }
        }

        if (query.postIds().size() != result.size()) {
            // 지금 단계 까지 없는 조회수 데이터는 0으로 처리
            Map<Long, Long> yetNotViewedList = query.postIds().stream()
                    .filter(postId -> !result.containsKey(postId))
                    .collect(
                            Collectors.toMap(
                                    postId -> postId,
                                    viewValue -> 0L
                            )
                    );

            result.putAll(yetNotViewedList);
        }

        return FindPostsViewByIdsQueryResult.builder()
                .viewCountDatas(result)
                .build();
    }
}
