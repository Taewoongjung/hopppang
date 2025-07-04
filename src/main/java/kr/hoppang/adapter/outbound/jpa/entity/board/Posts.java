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
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false, columnDefinition = "bigint")
    private Long boardId;

    @Column(name = "register_id", nullable = false, columnDefinition = "bigint")
    private Long registerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_anonymous", nullable = false, columnDefinition = "char(1)")
    private BoolType isAnonymous;


    private Posts(
            final Long id,
            final Long boardId,
            final Long registerId,
            final String title,
            final String contents,
            final BoolType isAnonymous
    ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.boardId = boardId;
        this.registerId = registerId;
        this.title = title;
        this.contents = contents;
        this.isAnonymous = isAnonymous;
    }

    public static Posts create(
            final Long boardId,
            final Long registerId,
            final String title,
            final String contents,
            final BoolType isAnonymous
    ) {
        return new Posts(
                null,
                boardId,
                registerId,
                title,
                contents,
                isAnonymous
        );
    }
}
