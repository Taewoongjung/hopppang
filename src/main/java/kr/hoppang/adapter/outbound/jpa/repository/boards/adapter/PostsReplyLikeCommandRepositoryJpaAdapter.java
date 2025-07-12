package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsReplyLikeJpaRepository;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import kr.hoppang.util.converter.boards.PostsReplyLikeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
@RequiredArgsConstructor
public class PostsReplyLikeCommandRepositoryJpaAdapter implements PostsReplyLikeCommandRepository {

    private final PostsReplyLikeJpaRepository postsReplyLikeJpaRepository;


    @Override
    public void create(final PostsReplyLike postsReplyLike) {
        postsReplyLikeJpaRepository.save(
                PostsReplyLikeConverter.toEntity(postsReplyLike)
        );
    }

    @Override
    public void delete(final PostsReplyLike postsReplyLike) {

    }
}
