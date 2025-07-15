package kr.hoppang.adapter.inbound.boards.readmodel.webdto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.springframework.web.bind.annotation.BindParam;

public record GetPostsByConditionWebDtoV1() {

    public record Req(
            @BindParam(value = "limit")
            @NotNull(message = "limit 값이 없습니다")
            Integer limit,

            @BindParam(value = "offset")
            @NotNull(message = "offset 값이 없습니다")
            Long offset,

            @BindParam(value = "boardIdList")
            List<Long> boardIdList
    ) { }

    @Builder
    public record Res(
            long count,
            List<PostWebDto> postsList
    ) {

        @Builder
        public record PostWebDto(
                Long id,
                Long boardId,
                String authorName,
                String title,
                String contents,
                Boolean isAnonymous,
                Boolean isRevised,
                LocalDateTime createdAt,
                Long viewCount
        ) { }
    }
}
