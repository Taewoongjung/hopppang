package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.command.boards.event.events.AddPostsLikeCountCommandEvent;
import kr.hoppang.application.readmodel.boards.queries.FindPostsCountOfLikesByIdsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindPostsCountOfLikesByIdsQueryResult;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindPostsCountOfLikesByIdsQueryHandler implements IQueryHandler<FindPostsCountOfLikesByIdsQuery, FindPostsCountOfLikesByIdsQueryResult> {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final List<PostsLikeQueryRepository> postsLikeQueryRepositoryList;
    private EnumMap<BoardsRepositoryStrategy, PostsLikeQueryRepository> postsLikeQueryRepositoryEnumMap;

    @PostConstruct
    void init() {
        postsLikeQueryRepositoryEnumMap = postsLikeQueryRepositoryList.stream()
                .collect(Collectors.toMap(
                        PostsLikeQueryRepository::strategy,
                        (postsLikeQueryRepository) -> postsLikeQueryRepository,
                        (existRepository, newRepository) -> existRepository,
                        () -> new EnumMap<>(BoardsRepositoryStrategy.class)
                ));
    }


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public FindPostsCountOfLikesByIdsQueryResult handle(final FindPostsCountOfLikesByIdsQuery query) {

        Map<Long, Long> countData = postsLikeQueryRepositoryEnumMap.get(
                        BoardsRepositoryStrategy.CACHE
                ).findCountOfLikesByPostIds(query.postIds());

        List<Long> notCachedCountsOfPostIdList = query.postIds().stream()
                .filter(postReplyId -> !countData.containsKey(postReplyId))
                .toList();

        if (!notCachedCountsOfPostIdList.isEmpty()) {
            Map<Long, Long> dataFromRDB = postsLikeQueryRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB)
                    .findCountOfLikesByPostIds(notCachedCountsOfPostIdList);

            countData.putAll(dataFromRDB);

            applicationEventPublisher.publishEvent(
                    AddPostsLikeCountCommandEvent.builder()
                            .countOfLikesByPostId(dataFromRDB)
                            .build()
            );
        }

        return FindPostsCountOfLikesByIdsQueryResult.builder()
                .countDatas(countData)
                .build();
    }
}
