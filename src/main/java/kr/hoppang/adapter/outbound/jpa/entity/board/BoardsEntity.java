package kr.hoppang.adapter.outbound.jpa.entity.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.boards.Boards;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_available", nullable = false, columnDefinition = "char(1)")
    private BoolType isAvailable;


    public Boards toPojo() {
        return Boards.builder()
                .id(this.id)
                .name(this.name)
                .isAvailable(this.isAvailable)
                .createdAt(this.getCreatedAt())
                .lastModified(this.getLastModified())
                .build();
    }
}
