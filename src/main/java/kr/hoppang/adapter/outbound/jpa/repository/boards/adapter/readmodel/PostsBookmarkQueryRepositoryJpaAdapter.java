package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.readmodel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsBookmarkJpaRepository;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostsIsBookmarkedDto;
import kr.hoppang.domain.boards.repository.PostsBookmarkQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsBookmarkQueryRepositoryJpaAdapter implements PostsBookmarkQueryRepository {

    private final PostsBookmarkJpaRepository postsBookmarkJpaRepository;


    @Override
    public Boolean isMarkedByPostsIdAndUserId(final Long postsId, final Long userId) {
        return postsBookmarkJpaRepository.existsById_PostIdAndId_UserId(postsId, userId);
    }

    @Override
    public Map<Long, Boolean> isBookmarkedGroupByPostsIdAndUserId(
            final List<Long> postsId,
            final Long userId
    ) {
        List<PostsIsBookmarkedDto> isBookmarkedDtoList = postsBookmarkJpaRepository.isBookmarkedGroupByPostIdAndUserId(postsId, userId);

        return isBookmarkedDtoList.stream()
                .collect(Collectors.toMap(
                        PostsIsBookmarkedDto::postId,
                        (e) -> e.bookmarkedCount() > 0
                ));
    }
}
