package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.command;

import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsBookmarkJpaRepository;
import kr.hoppang.domain.boards.PostsBookmark;
import kr.hoppang.domain.boards.repository.PostsBookmarkCommandRepository;
import kr.hoppang.util.converter.boards.PostsBookmarkConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
public class PostsBookmarkCommandRepositoryJpaAdapter implements PostsBookmarkCommandRepository {

    private final PostsBookmarkJpaRepository postsBookmarkJpaRepository;


    @Override
    @Transactional
    public void create(final PostsBookmark postsBookmark) {
        postsBookmarkJpaRepository.save(
                PostsBookmarkConverter.toEntity(postsBookmark)
        );
    }

    @Override
    @Transactional
    public void delete(final PostsBookmark postsBookmark) {
        postsBookmarkJpaRepository.delete(
                PostsBookmarkConverter.toEntity(postsBookmark)
        );
    }
}
