package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.command;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsReplyLikeJpaRepository;
import kr.hoppang.domain.boards.PostsReplyLike;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsReplyLikeCommandRepository;
import kr.hoppang.util.converter.boards.PostsReplyLikeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@Transactional
@RequiredArgsConstructor
public class PostsReplyLikeCommandRepositoryJpaAdapter implements PostsReplyLikeCommandRepository {

    private final PostsReplyLikeJpaRepository postsReplyLikeJpaRepository;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public void create(final PostsReplyLike postsReplyLike) {
        postsReplyLikeJpaRepository.save(
                PostsReplyLikeConverter.toEntity(postsReplyLike)
        );
    }

    @Override
    public void createAll(final List<PostsReplyLike> postsReplyLikeList) {
        if (postsReplyLikeList == null || postsReplyLikeList.isEmpty()) {
            return;
        }

        postsReplyLikeJpaRepository.saveAll(
                postsReplyLikeList.stream()
                        .map(PostsReplyLikeConverter::toEntity)
                        .toList()
        );
    }

    @Override
    public void delete(final PostsReplyLike postsReplyLike) {
        postsReplyLikeJpaRepository.delete(
                PostsReplyLikeConverter.toEntity(postsReplyLike)
        );
    }
}
