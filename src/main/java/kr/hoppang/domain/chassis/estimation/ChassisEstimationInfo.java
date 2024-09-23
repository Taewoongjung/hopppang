package kr.hoppang.domain.chassis.estimation;

import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChassisEstimationInfo {

    private final Long id;
    private final Long userId;
    private final ChassisEstimationAddress chassisEstimationAddress;
    private final CompanyType companyType;
    private final ChassisType chassisType;
    private final int width;
    private final int height;
    private final int laborFee;
    private final int ladderCarFee;
    private final int demolitionFee;
    private final int maintenanceFee;
    private final int freightTransportFee;
    private final int deliveryFee;
    private final int price;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModified;

    private ChassisEstimationInfo(
            final Long id,
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final CompanyType companyType,
            final ChassisType chassisType,
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
        this.companyType = companyType;
        this.chassisType = chassisType;
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
            final CompanyType companyType,
            final ChassisType chassisType,
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
                companyType,
                chassisType,
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
            final CompanyType companyType,
            final ChassisType chassisType,
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
                companyType,
                chassisType,
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
