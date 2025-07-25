package kr.hoppang.adapter.inbound.boards.webdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record AddPostWebDtoV1() {

    public record Req(

            @NotNull(message = "카테고리 정보가 없습니다")
            long boardId,

            @NotBlank(message = "title 이 없습니다")
            @Size(
                    min = 1,
                    max = 500,
                    message = "글 제목은 최소 {min} 최대 {max}개를 초과할 수 없습니다."
            )
            String title,

            @NotBlank(message = "contents 가 없습니다")
            @Size(
                    min = 1,
                    max = 3000,
                    message = "글 내용은 최소 {min} 최대 {max}개를 초과할 수 없습니다."
            )
            String contents,

            Boolean isAnonymous

    ) { }


    @Builder
    public record Res(
            long createdPostId
    ) { }
}
