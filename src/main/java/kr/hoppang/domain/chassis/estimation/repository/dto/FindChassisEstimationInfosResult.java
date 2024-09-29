package kr.hoppang.domain.chassis.estimation.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import lombok.Getter;

@Getter
public class FindChassisEstimationInfosResult {

    private final Long id;
    private final ChassisType chassisType;
    private final int width;
    private final int height;
    private final int price;
    private final Long userId;
    private final ChassisEstimationAddress chassisEstimationAddress;
    private final CompanyType companyType;
    private final int laborFee;
    private final int ladderCarFee;
    private final int demolitionFee;
    private final int maintenanceFee;
    private final int freightTransportFee;
    private final int deliveryFee;
    private final int totalPrice;
    private final String address;
    private final String subAddress;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModified;

    @QueryProjection
    public FindChassisEstimationInfosResult(Long id, ChassisType chassisType, int width, int height,
            int price, Long userId, ChassisEstimationAddress chassisEstimationAddress,
            CompanyType companyType, int laborFee, int ladderCarFee, int demolitionFee,
            int maintenanceFee, int freightTransportFee, int deliveryFee, int totalPrice,
            String address, String subAddress, LocalDateTime createdAt,
            LocalDateTime lastModified) {
        this.id = id;
        this.chassisType = chassisType;
        this.width = width;
        this.height = height;
        this.price = price;
        this.userId = userId;
        this.chassisEstimationAddress = chassisEstimationAddress;
        this.companyType = companyType;
        this.laborFee = laborFee;
        this.ladderCarFee = ladderCarFee;
        this.demolitionFee = demolitionFee;
        this.maintenanceFee = maintenanceFee;
        this.freightTransportFee = freightTransportFee;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
        this.address = address;
        this.subAddress = subAddress;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }
}
