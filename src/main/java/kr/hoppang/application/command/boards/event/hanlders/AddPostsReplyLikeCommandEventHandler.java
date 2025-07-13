package kr.hoppang.application.command.boards.event.hanlders;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.application.command.boards.event.events.AddPostsReplyLikeCommandEvent;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddPostsReplyLikeCommandEventHandler {

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
    public void handle(final AddPostsReplyLikeCommandEvent command) {

        try {
            postsReplyLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.CACHE)
                    .create(
                            PostsReplyLike.builder()
                                    .postReplyId(command.replyId())
                                    .userId(command.likedUserId())
                                    .clickedAt(command.clickedAt())
                                    .build()
                    );

        } catch (Exception e) {
            log.warn("{} 원인으로 캐싱 실패. 이후 디비 직접 반영", e.getMessage());

            postsReplyLikeCommandRepositoryEnumMap.get(BoardsRepositoryStrategy.RDB)
                    .create(
                            PostsReplyLike.builder()
                                    .postReplyId(command.replyId())
                                    .userId(command.likedUserId())
                                    .clickedAt(command.clickedAt())
                                    .build()
                    );
        }
    }
}