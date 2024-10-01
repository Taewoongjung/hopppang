package kr.hoppang.domain.chassis.estimation.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.Getter;

@Getter
public class FindChassisEstimationInfosResult {

    private Long id;
    private ChassisType chassisType;
    private int width;
    private int height;
    private int price;
    private Long userId;
    private CompanyType companyType;
    private int laborFee;
    private int ladderCarFee;
    private int demolitionFee;
    private int maintenanceFee;
    private int freightTransportFee;
    private int deliveryFee;
    private int totalPrice;
    private String address;
    private String subAddress;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    @QueryProjection
    public FindChassisEstimationInfosResult(
            Long id, ChassisType chassisType, int width, int height,
            int price, Long userId, CompanyType companyType, int laborFee, int ladderCarFee,
            int demolitionFee, int maintenanceFee, int freightTransportFee, int deliveryFee,
            int totalPrice, String address, String subAddress, LocalDateTime createdAt,
            LocalDateTime lastModified) {
        this.id = id;
        this.chassisType = chassisType;
        this.width = width;
        this.height = height;
        this.price = price;
        this.userId = userId;
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