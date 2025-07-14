package kr.hoppang.util.converter.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.view.PostsViewEntity;
import kr.hoppang.adapter.outbound.jpa.entity.board.view.PostsViewId;
import kr.hoppang.domain.boards.PostsView;

public class PostsViewConverter {

    public static PostsViewEntity toEntity(final PostsView pojo) {
        return PostsViewEntity.builder()
                .id(
                        PostsViewId.builder()
                                .postId(pojo.getPostId())
                                .clickedAt(pojo.getClickedAt())
                                .build()
                )
                .userId(pojo.getUserId())
                .build();
    }
}
