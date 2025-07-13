package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsReplyLikeJpaRepository;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.ReplyLikeCountDto;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsReplyLikeQueryRepositoryJpaAdapter implements PostsReplyLikeQueryRepository {

    private final PostsReplyLikeJpaRepository postsReplyLikeJpaRepository;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public Map<Long, Long> findCountOfLikesByPostId(final List<Long> replyIds) {

        List<ReplyLikeCountDto> replyLikeCountDtoList = postsReplyLikeJpaRepository.countLikesGroupedByReplyId(
                replyIds);

        return replyLikeCountDtoList.stream()
                .collect(Collectors.toMap(
                                ReplyLikeCountDto::postReplyId,
                                ReplyLikeCountDto::count
                        )
                );
    }
}
