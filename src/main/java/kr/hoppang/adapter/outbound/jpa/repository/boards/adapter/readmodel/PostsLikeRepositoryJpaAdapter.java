package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.readmodel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsLikeJpaRepository;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostLikeCountDto;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsLikeRepositoryJpaAdapter implements PostsLikeQueryRepository {

    private final PostsLikeJpaRepository postsLikeJpaRepository;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public Map<Long, Long> findCountOfLikesByPostIds(final List<Long> postIds) {

        List<PostLikeCountDto> postLikeCountDtoResult = postsLikeJpaRepository.countLikesGroupedByPostIds(
                postIds);

        return postLikeCountDtoResult.stream()
                .collect(
                        Collectors.toMap(
                                PostLikeCountDto::postId,
                                PostLikeCountDto::viewCount
                        )
                );
    }

    @Override
    public Boolean isLikedByPostId(final Long postId, final Long loggedInUserId) {
        return postsLikeJpaRepository.existsById_PostIdAndId_UserId(postId, loggedInUserId);
    }
}
