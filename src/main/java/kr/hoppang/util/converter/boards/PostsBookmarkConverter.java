package kr.hoppang.util.converter.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.bookmark.PostsBookmarkEntity;
import kr.hoppang.adapter.outbound.jpa.entity.board.bookmark.PostsBookmarkId;
import kr.hoppang.domain.boards.PostsBookmark;

public class PostsBookmarkConverter {

    public static PostsBookmarkEntity toEntity(final PostsBookmark pojo) {
        return PostsBookmarkEntity.builder()
                .id(
                        PostsBookmarkId.builder()
                                .postId(pojo.postId())
                                .userId(pojo.userId())
                                .build()
                )
                .clickedAt(pojo.clickedAt())
                .build();
    }
}
