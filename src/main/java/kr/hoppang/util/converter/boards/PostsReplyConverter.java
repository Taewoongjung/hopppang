package kr.hoppang.util.converter.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.PostsReplyEntity;
import kr.hoppang.domain.boards.PostsReply;

public class PostsReplyConverter {

    public static PostsReplyEntity toEntity(final PostsReply pojo) {
        return PostsReplyEntity.builder()
                .id(pojo.getId())
                .postId(pojo.getPostId())
                .rootReplyId(pojo.getRootReplyId())
                .contents(pojo.getContents())
                .registerId(pojo.getRegisterId())
                .isAnonymous(pojo.getIsAnonymous())
                .isDeleted(pojo.getIsDeleted())
                .build();
    }
}
