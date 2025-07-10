package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import kr.hoppang.util.common.BoolType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostsReply {

    private final Long id;
    private final Long postId;
    private final Long rootReplyId;
    private final String contents;
    private final Long registerId;
    private final BoolType isAnonymous;
    private final BoolType isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModified;
}
