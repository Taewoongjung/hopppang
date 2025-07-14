package kr.hoppang.application.command.boards.event.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.application.command.boards.event.events.RemovePostsLikeCommandEvent;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemovePostsLikeCommandEventHandler {

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
    public void handle(final RemovePostsLikeCommandEvent command) {

        try {

            postsLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.CACHE).delete(
                    PostsLike.builder()
                            .postId(command.postId())
                            .userId(command.unlikedUserId())
                            .build()
            );

            postsLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB).delete(
                    PostsLike.builder()
                            .postId(command.postId())
                            .userId(command.unlikedUserId())
                            .build()
            );

        } catch (Exception ignored) {}
    }
}
