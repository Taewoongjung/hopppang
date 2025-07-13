package kr.hoppang.application.command.boards.event.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.application.command.boards.event.events.RemovePostsReplyLikeCommandEvent;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemovePostsReplyLikeCommandEventHandler {

    private final List<PostsReplyLikeCommandRepository> postsReplyLikeCommandRepositoryList;
    private EnumMap<BoardsRepositoryStrategy, PostsReplyLikeCommandRepository> postsReplyLikeCommandRepositoryEnumMap;

    @PostConstruct
    void init() {
        postsReplyLikeCommandRepositoryEnumMap = postsReplyLikeCommandRepositoryList.stream()
                .collect(Collectors.toMap(
                        PostsReplyLikeCommandRepository::strategy,
                        (postsReplyLikeCommandRepository) -> postsReplyLikeCommandRepository,
                        (existRepository, newRepository) -> existRepository,
                        () -> new EnumMap<>(BoardsRepositoryStrategy.class)
                ));
    }


    @Async
    @EventListener
    public void handle(final RemovePostsReplyLikeCommandEvent command) {

        try{

            postsReplyLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.CACHE)
                    .delete(
                            PostsReplyLike.builder()
                                    .postReplyId(command.replyId())
                                    .userId(command.unlikedUserId())
                                    .build()
                    );

            postsReplyLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB)
                    .delete(
                            PostsReplyLike.builder()
                                    .postReplyId(command.replyId())
                                    .userId(command.unlikedUserId())
                                    .build()
                    );
        } catch (Exception ignored) {}
    }
}