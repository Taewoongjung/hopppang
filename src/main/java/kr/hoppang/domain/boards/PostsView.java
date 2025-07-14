package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostsView {

    private Long postId;
    private LocalDateTime clickedAt;
    private Long userId;
    private Long originCount; // 원래 카운트 갯수
}
