package kr.hoppang.adapter.inbound.boards.webdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.bind.annotation.BindParam;

public record AddPostWebDtoV1() {

    public record Req(

            @BindParam(value = "boardId")
            long boardId,

            @BindParam(value = "title")
            @NotBlank(message = "title 이 없습니다")
            @Size(
                    min = 1,
                    max = 100,
                    message = "글 제목은 최소 {min} 최대 {max}개를 초과할 수 없습니다."
            )
            String title,

            @BindParam(value = "contents")
            @NotBlank(message = "contents 가 없습니다")
            @Size(
                    min = 1,
                    max = 1000,
                    message = "글 내용은 최소 {min} 최대 {max}개를 초과할 수 없습니다."
            )
            String contents,

            @BindParam(value = "isAnonymous")
            boolean isAnonymous

    ) { }


    @Builder
    public record Res(
            long createdPostId
    ) { }
}
