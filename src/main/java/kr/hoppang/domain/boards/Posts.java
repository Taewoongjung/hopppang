package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import kr.hoppang.util.common.BoolType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Posts {

    private final Long id;
    private final Long boardId;
    private final Long registerId;
    private final String title;
    private final String contents;
    private final BoolType isAnonymous;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModified;
}
