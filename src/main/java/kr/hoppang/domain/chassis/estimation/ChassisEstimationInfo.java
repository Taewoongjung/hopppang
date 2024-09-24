package kr.hoppang.domain.chassis.estimation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final int laborFee;
    private final int ladderCarFee;
    private final int demolitionFee;
    private final int maintenanceFee;
    private final int freightTransportFee;
    private final int deliveryFee;
    private final int price;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModified;

    private List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList = new ArrayList<>();

    private ChassisEstimationInfo(
            final Long id,
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final CompanyType companyType,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int price,
            final List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        this.id = id;
        this.userId = userId;
        this.chassisEstimationAddress = chassisEstimationAddress;
        this.companyType = companyType;
        this.laborFee = laborFee;
        this.ladderCarFee = ladderCarFee;
        this.demolitionFee = demolitionFee;
        this.maintenanceFee = maintenanceFee;
        this.freightTransportFee = freightTransportFee;
        this.deliveryFee = deliveryFee;
        this.price = price;
        this.chassisEstimationSizeInfoList = chassisEstimationSizeInfoList;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    // 생성
    public static ChassisEstimationInfo of(
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final CompanyType companyType,
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
                laborFee,
                ladderCarFee,
                demolitionFee,
                maintenanceFee,
                freightTransportFee,
                deliveryFee,
                price,
                null,
                null, null
        );
    }

    // 조회
    public static ChassisEstimationInfo of(
            final Long id,
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final CompanyType companyType,
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
                laborFee,
                ladderCarFee,
                demolitionFee,
                maintenanceFee,
                freightTransportFee,
                deliveryFee,
                price,
                null,
                createdAt, lastModified
        );
    }
    public static ChassisEstimationInfo of(
            final Long id,
            final Long userId,
            final ChassisEstimationAddress chassisEstimationAddress,
            final CompanyType companyType,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int price,
            final List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        return new ChassisEstimationInfo(
                id,
                userId,
                chassisEstimationAddress,
                companyType,
                laborFee,
                ladderCarFee,
                demolitionFee,
                maintenanceFee,
                freightTransportFee,
                deliveryFee,
                price,
                chassisEstimationSizeInfoList,
                createdAt, lastModified
        );
    }
}
