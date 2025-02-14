package kr.hoppang.adapter.outbound.jpa.entity.chassis.price.pricecriteria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "additional_chassis_price_criteria")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalChassisPriceCriteriaEntity extends BaseEntity {

    /*
    * 이 테이블은 최종 창호 가격을 측정할 때 부가적으로 기준이 되는 필수 값임.
    * 그래서 디비에서 관리 함. 이 값은 수동으로 추가 됨.
    * */

    @Id
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20)")
    private AdditionalChassisPriceCriteriaType type;

    @Column(name = "price", nullable = false, columnDefinition = "INT UNSIGNED")
    private int price;

    public AdditionalChassisPriceCriteria toPojo() {
        return AdditionalChassisPriceCriteria.of(
                this.type, this.price, this.getLastModified(), this.getCreatedAt()
        );
    }

    public void revisePrice(final int price) {
        this.price = price;
    }

    public void updateLastModified() {
        updateLastModifiedAsNow();
    }
}
