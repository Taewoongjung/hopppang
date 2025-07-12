package kr.hoppang.adapter.outbound.jpa.entity.board.like;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class PostsReplyLikeId implements Serializable {

    @Column(name = "post_reply_id", nullable = false, columnDefinition = "bigint")
    private Long postReplyId;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;


    @Builder
    public PostsReplyLikeId(
            final Long postReplyId,
            final Long userId
    ) {
        this.postReplyId = postReplyId;
        this.userId = userId;
    }
}