package kr.hoppang.application.command.boards.event.hanlders;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.application.command.boards.event.events.AddPostsViewCommandEvent;
import kr.hoppang.domain.boards.PostsView;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsViewCommandRepository;
import kr.hoppang.domain.boards.repository.PostsViewQueryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AddPostsViewCommandEventHandler {

    private final PostsViewCommandRepository postsViewCommandCacheRepository;
    private final EnumMap<BoardsRepositoryStrategy, PostsViewQueryRepository> postsViewQueryRepositoryEnumMap;

    public AddPostsViewCommandEventHandler(
            final List<PostsViewQueryRepository> postsViewQueryRepositoryList,
            @Qualifier(value = "PostsViewCommandRepositoryRedis") final PostsViewCommandRepository postsViewCommandRepository
    ) {
        this.postsViewCommandCacheRepository = postsViewCommandRepository;
        postsViewQueryRepositoryEnumMap = postsViewQueryRepositoryList.stream()
                .collect(Collectors.toMap(
                        PostsViewQueryRepository::strategy,
                        (postsViewQueryRepository) -> postsViewQueryRepository,
                        (existRepository, newRepository) -> existRepository,
                        () -> new EnumMap<>(BoardsRepositoryStrategy.class)
                ));
    }


    @Async
    @EventListener
    public void handle(final AddPostsViewCommandEvent event) {

        boolean cacheRemained = true;

        Map<Long, Long> viewCount = postsViewQueryRepositoryEnumMap.get(
                        BoardsRepositoryStrategy.CACHE)
                .findCountOfViewsByPostIds(List.of(event.postId()));

        if (viewCount == null || viewCount.isEmpty()) {
            cacheRemained = false;
            viewCount = postsViewQueryRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB)
                    .findCountOfViewsByPostIds(List.of(event.postId()));
        }

        postsViewCommandCacheRepository.create(
                PostsView.builder()
                        .postId(event.postId())
                        .clickedAt(event.clickedAt())
                        .userId(event.userId())
                        .originCount(
                                // 아직 캐시가 남아 있다면 null로 넘긴다.
                                // 이유는 디비에 반영 된 값을 넘기면 "해당 값 + 캐시에 남겨진 값"으로 되어버리니까 제곱이 되어 버리기 때문
                                cacheRemained ?
                                        null : viewCount.get(event.postId())
                        )
                        .build()
        );
    }
}
