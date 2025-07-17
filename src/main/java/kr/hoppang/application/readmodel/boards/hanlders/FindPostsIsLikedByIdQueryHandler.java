package kr.hoppang.application.readmodel.boards.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindPostsIsLikedByIdQuery;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPostsIsLikedByIdQueryHandler implements IQueryHandler<FindPostsIsLikedByIdQuery, Boolean> {

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
    public Boolean handle(final FindPostsIsLikedByIdQuery query) {

        Boolean didILikeFromCache = postsLikeQueryRepositoryEnumMap.get(
                BoardsRepositoryStrategy.CACHE
        ).isLikedByPostId(query.postId(), query.loggedInUserId());

        if (didILikeFromCache == null) {
            didILikeFromCache = postsLikeQueryRepositoryEnumMap.get(
                    BoardsRepositoryStrategy.RDB
            ).isLikedByPostId(query.postId(), query.loggedInUserId());
        }

        return didILikeFromCache;
    }
}
