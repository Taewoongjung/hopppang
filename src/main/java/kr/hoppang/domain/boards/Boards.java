package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import kr.hoppang.util.common.BoolType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Boards {

    private final Long id;
    private final Long rootBoardId;
    private final String name;
    private final BoolType isAvailable;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModified;
}
