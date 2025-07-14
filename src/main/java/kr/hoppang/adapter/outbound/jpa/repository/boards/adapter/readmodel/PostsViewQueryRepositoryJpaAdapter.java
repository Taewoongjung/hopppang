package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.readmodel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsViewJpaRepository;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostViewCountDto;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsViewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository("PostsViewQueryRepositoryJpa")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsViewQueryRepositoryJpaAdapter implements PostsViewQueryRepository {

    private final PostsViewJpaRepository postsViewJpaRepository;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public Map<Long, Long> findCountOfViewsByPostIds(final List<Long> postIds) {
         List<PostViewCountDto> postViewCountDtoList = postsViewJpaRepository.countViewsGroupedByPostIds(postIds);

        return postViewCountDtoList.stream()
                .collect(Collectors.toMap(
                                PostViewCountDto::postId,
                                PostViewCountDto::viewCount
                        )
                );
    }
}
