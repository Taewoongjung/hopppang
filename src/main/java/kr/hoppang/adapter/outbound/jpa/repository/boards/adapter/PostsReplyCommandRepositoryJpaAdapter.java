package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import kr.hoppang.adapter.outbound.jpa.entity.board.PostsReplyEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsReplyJpaRepository;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.PostsReplyCommandRepository;
import kr.hoppang.util.converter.boards.PostsReplyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@Transactional
@RequiredArgsConstructor
public class PostsReplyCommandRepositoryJpaAdapter implements PostsReplyCommandRepository {

    private final PostsReplyJpaRepository postsReplyJpaRepository;


    @Override
    public Long create(final PostsReply postsReply) {

        PostsReplyEntity postsReplyEntity = postsReplyJpaRepository.save(
                PostsReplyConverter.toEntity(postsReply));

        return postsReplyEntity.getId();
    }
}
