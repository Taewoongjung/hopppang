package kr.hoppang.domain.chassis.estimation;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChassisEstimationInfo {

    private Long id;
    private Long userId;
    private ChassisEstimationAddress chassisEstimationAddress;
    private int width;
    private int height;
    private int laborFee;
    private int ladderCarFee;
    private int demolitionFee;
    private int maintenanceFee;
    private int freightTransportFee;
    private int deliveryFee;
    private int price;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    private ChassisEstimationInfo(
            final Long id,
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final int width,
            final int height,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int price,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        this.id = id;
        this.userId = userId;
        this.chassisEstimationAddress = chassisEstimationAddress;
        this.width = width;
        this.height = height;
        this.laborFee = laborFee;
        this.ladderCarFee = ladderCarFee;
        this.demolitionFee = demolitionFee;
        this.maintenanceFee = maintenanceFee;
        this.freightTransportFee = freightTransportFee;
        this.deliveryFee = deliveryFee;
        this.price = price;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    // 생성
    public static ChassisEstimationInfo of(
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final int width,
            final int height,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int price
    ) {
        return new ChassisEstimationInfo(
                null,
                userId,
                chassisEstimationAddress,
                width,
                height,
                laborFee,
                ladderCarFee,
                demolitionFee,
                maintenanceFee,
                freightTransportFee,
                deliveryFee,
                price,
                null, null
        );
    }

    // 조회
    public static ChassisEstimationInfo of(
            final Long id,
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final int width,
            final int height,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int price,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        return new ChassisEstimationInfo(
                id,
                userId,
                chassisEstimationAddress,
                width,
                height,
                laborFee,
                ladderCarFee,
                demolitionFee,
                maintenanceFee,
                freightTransportFee,
                deliveryFee,
                price,
                createdAt, lastModified
        );
    }
}
