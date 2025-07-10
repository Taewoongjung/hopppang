package kr.hoppang.adapter.outbound.jpa.entity.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts_reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostsReplyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false, columnDefinition = "bigint")
    private Long postId;

    @Column(name = "root_reply_id", columnDefinition = "bigint")
    private Long rootReplyId;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "register_id", nullable = false, columnDefinition = "bigint")
    private Long registerId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_anonymous", nullable = false, columnDefinition = "char(1)")
    private BoolType isAnonymous;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_deleted", nullable = false, columnDefinition = "char(1)")
    private BoolType isDeleted;


    @Builder
    private PostsReplyEntity(
            final Long id,
            final Long postId,
            final Long rootReplyId,
            final String contents,
            final Long registerId,
            final BoolType isAnonymous,
            final BoolType isDeleted
    ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.postId = postId;
        this.rootReplyId = rootReplyId;
        this.contents = contents;
        this.registerId = registerId;
        this.isAnonymous = isAnonymous;
        this.isDeleted = isDeleted;
    }

    public static PostsReplyEntity create(
            final Long postId,
            final Long rootReplyId,
            final String contents,
            final Long registerId,
            final BoolType isAnonymous
    ) {
        return new PostsReplyEntity(
                null,
                postId,
                rootReplyId,
                contents,
                registerId,
                isAnonymous,
                BoolType.F
        );
    }

    public PostsReply toPojo() {
        return PostsReply.builder()
                .id(this.id)
                .postId(this.postId)
                .rootReplyId(this.rootReplyId)
                .contents(this.contents)
                .registerId(this.registerId)
                .isAnonymous(this.isAnonymous)
                .isDeleted(this.isDeleted)
                .createdAt(this.getCreatedAt())
                .lastModified(this.getLastModified())
                .build();
    }
}
