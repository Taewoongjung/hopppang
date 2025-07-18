package kr.hoppang.adapter.inbound.boards.webdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record RevisePostReplyWebDtoV1() {

    @Builder
    public record Req(

            @NotBlank(message = "contents 가 없습니다")
            @Size(
                    min = 1,
                    max = 500,
                    message = "댓글 내용은 최소 {min} 최대 {max}개를 초과할 수 없습니다."
            )
            String contents

    ) { }
}
