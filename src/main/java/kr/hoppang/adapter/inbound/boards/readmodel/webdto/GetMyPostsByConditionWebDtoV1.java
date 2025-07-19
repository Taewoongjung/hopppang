package kr.hoppang.adapter.inbound.boards.readmodel.webdto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.springframework.web.bind.annotation.BindParam;

public record GetMyPostsByConditionWebDtoV1() {

    public record Req(
            @BindParam(value = "limit")
            @NotNull(message = "limit 값이 없습니다")
            Integer limit,

            @BindParam(value = "offset")
            @NotNull(message = "offset 값이 없습니다")
            Long offset,

            @BindParam(value = "searchWord")
            String searchWord,

            @BindParam(value = "boardIdList")
            List<Long> boardIdList
    ) { }

    @Builder
    public record Res(
            long count,
            List<MyPostWebDto> postsList
    ) {

        @Builder
        public record MyPostWebDto(
                Long id,
                Long boardId,
                String authorName,
                String title,
                String contents,
                Boolean isAnonymous,
                LocalDateTime createdAt,
                Long viewCount,
                Long likeCount,
                Long replyCount
        ) { }
    }
}
