package kr.hoppang.util.converter.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsReplyLikeEntity;
import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsReplyLikeId;
import kr.hoppang.domain.boards.PostsReplyLike;

public class PostsReplyLikeConverter {

    public static PostsReplyLikeEntity toEntity(final PostsReplyLike pojo) {
        return PostsReplyLikeEntity.builder()
                .id(
                        PostsReplyLikeId.builder()
                                .postReplyId(pojo.postReplyId())
                                .userId(pojo.userId())
                                .build()
                )
                .clickedAt(pojo.clickedAt())
                .build();
    }
}
