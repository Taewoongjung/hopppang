package kr.hoppang.application.command.boards.event.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.application.command.boards.event.events.AddPostsLikeCommandEvent;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddPostsLikeCommandEventHandler {

    private final List<PostsLikeCommandRepository> postsLikeCommandRepositoryList;
    private EnumMap<BoardsRepositoryStrategy, PostsLikeCommandRepository> postsLikeCommandRepositoryEnumMap;

    @PostConstruct
    void init() {
        postsLikeCommandRepositoryEnumMap = postsLikeCommandRepositoryList.stream()
                .collect(Collectors.toMap(
                        PostsLikeCommandRepository::strategy,
                        (postsLikeCommandRepository) -> postsLikeCommandRepository,
                        (existRepository, newRepository) -> existRepository,
                        () -> new EnumMap<>(BoardsRepositoryStrategy.class)
                ));
    }


    @Async
    @EventListener
    public void handle(final AddPostsLikeCommandEvent command) {

        try {
            postsLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.CACHE)
                    .create(
                            PostsLike.builder()
                                    .postId(command.postId())
                                    .userId(command.likedUserId())
                                    .clickedAt(command.clickedAt())
                                    .build()
                    );

        } catch (Exception e) {
            log.warn("{} 원인으로 캐싱 실패. 이후 디비 직접 반영", e.getMessage());

            postsLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB)
                    .create(
                            PostsLike.builder()
                                    .postId(command.postId())
                                    .userId(command.likedUserId())
                                    .clickedAt(command.clickedAt())
                                    .build()
                    );
        }
    }
}
