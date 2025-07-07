package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsJpaRepository;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsCommandRepository;
import kr.hoppang.util.converter.boards.PostsConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostsCommandRepositoryJpaAdapter implements PostsCommandRepository {

    private final PostsJpaRepository postsJpaRepository;


    @Override
    public void create(final Posts newPost) {
        postsJpaRepository.save(PostsConverter.toEntity(newPost));
    }
}
