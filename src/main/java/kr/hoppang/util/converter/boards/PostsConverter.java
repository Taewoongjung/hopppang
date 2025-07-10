package kr.hoppang.util.converter.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.PostsEntity;
import kr.hoppang.domain.boards.Posts;

public class PostsConverter {

    public static PostsEntity toEntity(final Posts pojo) {
        return PostsEntity.builder()
                .id(pojo.getId())
                .boardId(pojo.getBoardId())
                .registerId(pojo.getRegisterId())
                .title(pojo.getTitle())
                .title(pojo.getTitle())
                .contents(pojo.getContents())
                .isAnonymous(pojo.getIsAnonymous())
                .isDeleted(pojo.getIsDeleted())
                .build();
    }
}
