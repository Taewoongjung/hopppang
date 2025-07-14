package kr.hoppang.adapter.outbound.jpa.entity.board.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "post_view")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostsViewEntity {

    @Id
    private PostsViewId id;

    @Column(name = "user_id", columnDefinition = "bigint")
    private Long userId;


    @Builder
    public PostsViewEntity(
            final PostsViewId id,
            final Long userId
    ) {
        this.id = id;
        this.userId = userId;
    }
}
