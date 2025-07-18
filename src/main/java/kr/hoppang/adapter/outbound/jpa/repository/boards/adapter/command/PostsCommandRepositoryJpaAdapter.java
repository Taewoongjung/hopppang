package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.command;

import kr.hoppang.adapter.outbound.jpa.entity.board.PostsEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsJpaRepository;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsCommandRepository;
import kr.hoppang.util.converter.boards.PostsConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@Transactional
@RequiredArgsConstructor
public class PostsCommandRepositoryJpaAdapter implements PostsCommandRepository {

    private final PostsJpaRepository postsJpaRepository;


    @Override
    public Long create(final Posts newPost) {
        PostsEntity createdEntity = postsJpaRepository.save(
                PostsConverter.toEntity(newPost));

        return createdEntity.getId();
    }

    @Override
    public boolean revise(final Posts revisedPost) {
        postsJpaRepository.save(PostsConverter.toEntity(revisedPost));

        return true;
    }
}
