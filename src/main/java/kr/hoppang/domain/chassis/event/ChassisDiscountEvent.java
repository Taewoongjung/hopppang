package kr.hoppang.domain.chassis.event;

import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.util.common.BoolType;
import lombok.Getter;

@Getter
public class ChassisDiscountEvent {

    private final Long id;
    private final CompanyType companyType;
    private final EventChassisType chassisType;
    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;
    private final int discountRate;
    private final DiscountType discountType;
    private final BoolType isStillOnDiscount;
    private final Long publisherId;

    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;


    private ChassisDiscountEvent(
            final Long id,
            final CompanyType companyType,
            final EventChassisType eventChassisType,
            final LocalDateTime startedAt,
            final LocalDateTime endedAt,
            final int discountRate,
            final DiscountType discountType,
            final BoolType isStillOnDiscount,
            final Long publisherId,
            final LocalDateTime createdAt,
            final LocalDateTime lastModifiedAt
    ) {

        this.id = id;
        this.companyType = companyType;
        this.chassisType = eventChassisType;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.discountRate = discountRate;
        this.discountType = discountType;
        this.isStillOnDiscount = isStillOnDiscount;
        this.publisherId = publisherId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }


    // 생성
    public static ChassisDiscountEvent of(
            final CompanyType companyType,
            final EventChassisType eventChassisType,
            final LocalDateTime startedAt,
            final LocalDateTime endedAt,
            final int discountRate,
            final DiscountType discountType,
            final BoolType isStillOnDiscount,
            final Long publisherId
    ) {
        return new ChassisDiscountEvent(
                null,
                companyType,
                eventChassisType,
                startedAt,
                endedAt,
                discountRate,
                discountType,
                isStillOnDiscount,
                publisherId,
                null, null
        );
    }

    // 조회
    public static ChassisDiscountEvent of(
            final Long id,
            final CompanyType companyType,
            final EventChassisType eventChassisType,
            final LocalDateTime startedAt,
            final LocalDateTime endedAt,
            final int discountRate,
            final DiscountType discountType,
            final BoolType isStillOnDiscount,
            final Long publisherId,
            final LocalDateTime createdAt,
            final LocalDateTime lastModifiedAt
    ) {
        return new ChassisDiscountEvent(
                id,
                companyType,
                eventChassisType,
                startedAt,
                endedAt,
                discountRate,
                discountType,
                isStillOnDiscount,
                publisherId,
                createdAt,
                lastModifiedAt
        );
    }
}
