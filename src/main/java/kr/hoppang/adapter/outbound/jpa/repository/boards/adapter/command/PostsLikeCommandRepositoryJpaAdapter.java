package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.command;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsLikeJpaRepository;
import kr.hoppang.domain.boards.PostsLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsLikeCommandRepository;
import kr.hoppang.util.converter.boards.PostsLikeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
@RequiredArgsConstructor
public class PostsLikeCommandRepositoryJpaAdapter implements PostsLikeCommandRepository {

    private final PostsLikeJpaRepository postsLikeJpaRepository;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public void create(final PostsLike postsLike) {
        postsLikeJpaRepository.save(
                PostsLikeConverter.toEntity(postsLike)
        );
    }

    @Override
    public void createAll(final List<PostsLike> postsLikes) {
        if (postsLikes == null || postsLikes.isEmpty()) {
            return;
        }

        postsLikeJpaRepository.saveAll(
                postsLikes.stream()
                        .map(PostsLikeConverter::toEntity)
                        .toList()
        );
    }

    @Override
    public void delete(final PostsLike postsLike) {
        postsLikeJpaRepository.delete(PostsLikeConverter.toEntity(postsLike));
    }
}
