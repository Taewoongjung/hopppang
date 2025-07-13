package kr.hoppang.domain.boards;

import java.time.LocalDateTime;
import kr.hoppang.util.common.BoolType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

    private Boolean amILiked;

    @Setter
    private Long likeCount;

    public boolean isParent() {
        return this.rootReplyId == null;
    }

    public boolean isChild() {
        return this.rootReplyId != null;
    }

    public void liked() {
        this.amILiked = true;
    }
}
