package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsJpaRepository;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostsQueryRepositoryJpaAdapter implements PostsQueryRepository {

    private final PostsJpaRepository postsJpaRepository;


}
