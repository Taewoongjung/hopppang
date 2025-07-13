package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindPostsRepliesLikeClickedByIdsQuery;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPostsRepliesLikeClickedByIdsQueryHandler implements IQueryHandler<FindPostsRepliesLikeClickedByIdsQuery, List<Long>> {

    private final List<PostsReplyQueryRepository> postsReplyQueryRepositoryList;
    private EnumMap<BoardsRepositoryStrategy, PostsReplyQueryRepository> postsReplyQueryRepositoryEnumMap;

    @PostConstruct
    void init() {
        postsReplyQueryRepositoryEnumMap = postsReplyQueryRepositoryList.stream()
                .collect(Collectors.toMap(
                        PostsReplyQueryRepository::strategy,
                        (postsReplyQueryRepository) -> postsReplyQueryRepository,
                        (existRepository, newRepository) -> existRepository,
                        () -> new EnumMap<>(BoardsRepositoryStrategy.class)
                ));
    }


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    public List<Long> handle(final FindPostsRepliesLikeClickedByIdsQuery query) {

        List<Long> likedReplyList =
                postsReplyQueryRepositoryEnumMap.get(BoardsRepositoryStrategy.CACHE)
                        .findAllLikedReplyIdsByUserId(
                                query.replyIdList(),
                                query.loggedUserId()
                        );

        if (likedReplyList.size() != query.replyIdList().size()) {
            likedReplyList.addAll(postsReplyQueryRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB)
                    .findAllLikedReplyIdsByUserId(
                            query.replyIdList(),
                            query.loggedUserId()
                    )
            );
        }

        return likedReplyList;
    }
}
