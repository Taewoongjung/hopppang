package kr.hoppang.util.converter.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsLikeEntity;
import kr.hoppang.adapter.outbound.jpa.entity.board.like.PostsLikeId;
import kr.hoppang.domain.boards.PostsLike;

public class PostsLikeConverter {

    public static PostsLikeEntity toEntity(final PostsLike pojo) {
        return PostsLikeEntity.builder()
                .id(
                        PostsLikeId.builder()
                                .postId(pojo.postId())
                                .userId(pojo.userId())
                                .build()
                )
                .clickedAt(pojo.clickedAt())
                .build();
    }
}
