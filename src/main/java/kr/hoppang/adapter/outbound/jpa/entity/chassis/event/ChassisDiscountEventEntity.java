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
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;
import kr.hoppang.domain.chassis.event.DiscountType;
import kr.hoppang.domain.chassis.event.EventChassisType;
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
    private EventChassisType chassisType;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private int discountRate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "discount_type", nullable = false, columnDefinition = "CHAR(12)")
    private DiscountType discountType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_still_on_discount", nullable = false, columnDefinition = "char(1)")
    private BoolType isStillOnDiscount;

    private Long publisherId;


    private ChassisDiscountEventEntity(
            final Long id,
            final CompanyType companyType,
            final EventChassisType eventChassisType,
            final LocalDateTime startedAt,
            final LocalDateTime endedAt,
            final int discountRate,
            final DiscountType discountType,
            final BoolType isStillOnDiscount,
            final Long publisherId
    ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.companyType = companyType;
        this.chassisType = eventChassisType;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.discountRate = discountRate;
        this.discountType = discountType;
        this.isStillOnDiscount = isStillOnDiscount;
        this.publisherId = publisherId;
    }

    public static ChassisDiscountEventEntity of(
            final CompanyType companyType,
            final EventChassisType eventChassisType,
            final LocalDateTime startedAt,
            final LocalDateTime endedAt,
            final int discountRate,
            final DiscountType discountType,
            final BoolType isStillOnDiscount,
            final Long publisherId
    ) {
        return new ChassisDiscountEventEntity(
                null,
                companyType,
                eventChassisType,
                startedAt,
                endedAt,
                discountRate,
                discountType,
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
                getDiscountType(),
                getIsStillOnDiscount(),
                getPublisherId(),
                getCreatedAt(),
                getLastModified()
        );
    }
}
