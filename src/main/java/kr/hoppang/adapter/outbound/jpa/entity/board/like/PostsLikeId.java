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
public class PostsLikeId implements Serializable {

    @Column(name = "post_id", nullable = false, columnDefinition = "bigint")
    private Long postId;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;


    @Builder
    public PostsLikeId(
            final Long postId,
            final Long userId
    ) {
        this.postId = postId;
        this.userId = userId;
    }
}
