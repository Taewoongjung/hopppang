package kr.hoppang.domain.chassis.estimation;

import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.ChassisType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ChassisEstimationSizeInfo {

    private final Long id;
    private final Long chassisEstimationInfoId;
    private final ChassisType chassisType;
    private final int width;
    private final int height;
    private final int price;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModified;

    private ChassisEstimationSizeInfo(
            final Long id,
            final Long chassisEstimationInfoId,
            final ChassisType chassisType,
            final int width,
            final int height,
            final int price,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        this.id = id;
        this.chassisEstimationInfoId = chassisEstimationInfoId;
        this.chassisType = chassisType;
        this.width = width;
        this.height = height;
        this.price = price;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    // 생성
    public static ChassisEstimationSizeInfo of(
            final ChassisType chassisType,
            final int width,
            final int height,
            final int price
    ) {
        return new ChassisEstimationSizeInfo(
                null,
                null,
                chassisType,
                width,
                height,
                price,
                null, null
        );
    }

    // 조회
    public static ChassisEstimationSizeInfo of(
            final Long id,
            final Long chassisEstimationInfoId,
            final ChassisType chassisType,
            final int width,
            final int height,
            final int price,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        return new ChassisEstimationSizeInfo(
                id,
                chassisEstimationInfoId,
                chassisType,
                width,
                height,
                price,
                createdAt, lastModified
        );
    }
}
