package kr.hoppang.adapter.inbound.boards.readmodel.webdto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import kr.hoppang.domain.boards.Posts;
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
            List<Posts> postsList,
            long count
    ) { }
}
