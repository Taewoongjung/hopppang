package kr.hoppang.util.converter.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.PostsEntity;
import kr.hoppang.domain.boards.Posts;

public class PostsConverter {

    public static PostsEntity toEntity(final Posts posts) {
        return PostsEntity.builder()
                .id(posts.getId())
                .boardId(posts.getBoardId())
                .registerId(posts.getRegisterId())
                .title(posts.getTitle())
                .title(posts.getTitle())
                .contents(posts.getContents())
                .isAnonymous(posts.getIsAnonymous())
                .isDeleted(posts.getIsDeleted())
                .build();
    }
}
