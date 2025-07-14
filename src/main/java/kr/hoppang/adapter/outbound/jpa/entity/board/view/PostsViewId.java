package kr.hoppang.adapter.outbound.jpa.entity.board.view;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PostsViewId implements Serializable {

    @Column(name = "post_id", nullable = false, columnDefinition = "bigint")
    private Long postId;

    @CreatedDate
    @Column(name = "clicked_at", nullable = false, updatable = false)
    private LocalDateTime clickedAt;
}
