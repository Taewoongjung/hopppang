package kr.hoppang.adapter.outbound.jpa.entity.chassis.event;

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
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "chassis_discount_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChassisDiscountEventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "company_type", nullable = false, columnDefinition = "varchar(20)")
    private CompanyType companyType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "chassis_type", nullable = false, columnDefinition = "varchar(20)")
    private ChassisType chassisType;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private int discountRate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_still_on_discount", nullable = false, columnDefinition = "char(1)")
    private BoolType isStillOnDiscount;

    private Long publisherId;


    private ChassisDiscountEventEntity(
            final Long id,
            final CompanyType companyType,
            final ChassisType chassisType,
            final LocalDateTime startedAt,
            final LocalDateTime endedAt,
            final int discountRate,
            final BoolType isStillOnDiscount,
            final Long publisherId
    ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.companyType = companyType;
        this.chassisType = chassisType;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.discountRate = discountRate;
        this.isStillOnDiscount = isStillOnDiscount;
        this.publisherId = publisherId;
    }

    public static ChassisDiscountEventEntity of(
            final CompanyType companyType,
            final ChassisType chassisType,
            final LocalDateTime startedAt,
            final LocalDateTime endedAt,
            final int discountRate,
            final BoolType isStillOnDiscount,
            final Long publisherId
    ) {
        return new ChassisDiscountEventEntity(
                null,
                companyType,
                chassisType,
                startedAt,
                endedAt,
                discountRate,
                isStillOnDiscount,
                publisherId
        );
    }

    public ChassisDiscountEvent toPojo() {
        return ChassisDiscountEvent.of(
                getId(),
                getCompanyType(),
                getChassisType(),
                getStartedAt(),
                getEndedAt(),
                getDiscountRate(),
                getIsStillOnDiscount(),
                getPublisherId(),
                getCreatedAt(),
                getLastModified()
        );
    }
}
