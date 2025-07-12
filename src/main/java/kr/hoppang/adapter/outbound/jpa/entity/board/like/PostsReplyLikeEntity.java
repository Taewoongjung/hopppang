package kr.hoppang.adapter.outbound.jpa.entity.board.like;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Table(name = "post_reply_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostsReplyLikeEntity {

    @EmbeddedId
    private PostsReplyLikeId id;

    @CreatedDate
    @Column(name = "clicked_at", nullable = false, updatable = false)
    private LocalDateTime clickedAt;


    @Builder
    public PostsReplyLikeEntity(
            PostsReplyLikeId id,
            LocalDateTime clickedAt
    ) {
        this.id = id;
        this.clickedAt = clickedAt;
    }
}