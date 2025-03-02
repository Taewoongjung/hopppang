package kr.hoppang.domain.chassis.estimation;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChassisEstimationInquiry {

    private final Long id;
    private final Long userId;
    private final Long chassisEstimationInfoId;
    private final String strategy;
    private final LocalDateTime createdAt;

    private ChassisEstimationInquiry(
            final Long id,
            final Long userId,
            final Long chassisEstimationInfoId,
            final String strategy,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.chassisEstimationInfoId = chassisEstimationInfoId;
        this.strategy = strategy;
        this.createdAt = createdAt;
    }

    public static ChassisEstimationInquiry of(
            final Long id,
            final Long userId,
            final Long chassisEstimationInfoId,
            final String strategy,
            final LocalDateTime createdAt
    ) {

        return new ChassisEstimationInquiry(
                id,
                userId,
                chassisEstimationInfoId,
                strategy,
                createdAt
        );
    }
}
