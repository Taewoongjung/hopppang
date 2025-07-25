package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.command;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsViewJpaRepository;
import kr.hoppang.domain.boards.PostsView;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsViewCommandRepository;
import kr.hoppang.util.converter.boards.PostsViewConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
@RequiredArgsConstructor
public class PostsVIewCommandRepositoryJpaAdapter implements PostsViewCommandRepository {

    private final PostsViewJpaRepository postsViewJpaRepository;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public void create(final PostsView postsView) {
        postsViewJpaRepository.save(
                PostsViewConverter.toEntity(postsView)
        );
    }

    @Override
    public void createAll(final List<PostsView> postsViewList) {
        postsViewJpaRepository.saveAll(
                postsViewList.stream()
                        .map(PostsViewConverter::toEntity)
                        .toList()
        );
    }
}
