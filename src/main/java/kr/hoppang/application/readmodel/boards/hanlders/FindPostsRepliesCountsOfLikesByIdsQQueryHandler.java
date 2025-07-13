package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.command.boards.event.events.AddPostsReplyLikeCountCommandEvent;
import kr.hoppang.application.readmodel.boards.queries.FindPostsRepliesCountsOfLikesByIdsQuery;
import kr.hoppang.application.readmodel.boards.queryresults.FindPostsRepliesCountsOfLikesByIdsQQueryResult;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindPostsRepliesCountsOfLikesByIdsQQueryHandler implements IQueryHandler<FindPostsRepliesCountsOfLikesByIdsQuery, FindPostsRepliesCountsOfLikesByIdsQQueryResult> {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final List<PostsReplyLikeQueryRepository> postsReplyLikeQueryRepositoryList;
    private EnumMap<BoardsRepositoryStrategy, PostsReplyLikeQueryRepository> postsReplyLikeQueryRepositoryEnumMap;

    @PostConstruct
    void init() {
        postsReplyLikeQueryRepositoryEnumMap = postsReplyLikeQueryRepositoryList.stream()
                .collect(Collectors.toMap(
                        PostsReplyLikeQueryRepository::strategy,
                        (postsReplyLikeQueryRepository) -> postsReplyLikeQueryRepository,
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
    public FindPostsRepliesCountsOfLikesByIdsQQueryResult handle(
            final FindPostsRepliesCountsOfLikesByIdsQuery query
    ) {

        Map<Long, Long> countDatas = postsReplyLikeQueryRepositoryEnumMap.get(
                BoardsRepositoryStrategy.CACHE
        ).findCountOfLikesByPostId(query.replyIdList());

        List<Long> notCachedCountsOfReplyIdList = query.replyIdList().stream()
                .filter(postId -> !countDatas.containsKey(postId))
                .toList();

        if (!notCachedCountsOfReplyIdList.isEmpty()) {
            Map<Long, Long> dataFromRDB = postsReplyLikeQueryRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB)
                    .findCountOfLikesByPostId(notCachedCountsOfReplyIdList);

            countDatas.putAll(dataFromRDB);

            applicationEventPublisher.publishEvent(
                    AddPostsReplyLikeCountCommandEvent.builder()
                            .countOfLikesByReplyId(dataFromRDB)
                            .build()
            );
        }

        return FindPostsRepliesCountsOfLikesByIdsQQueryResult.builder()
                .countDatas(countDatas)
                .build();
    }
}
