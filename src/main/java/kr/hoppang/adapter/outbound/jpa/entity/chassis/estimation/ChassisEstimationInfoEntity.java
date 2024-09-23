package kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "chassis_estimation_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChassisEstimationInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long chassisEstimationAddressId;
    private CompanyType companyType;
    private ChassisType chassisType;
    private int width;
    private int height;
    private int laborFee;
    private int ladderCarFee;
    private int demolitionFee;
    private int maintenanceFee;
    private int freightTransportFee;
    private int deliveryFee;
    private int price;


    private ChassisEstimationInfoEntity(
            final Long id,
            final Long userId,
            final Long chassisEstimationAddressId,
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

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.userId = userId;
        this.chassisEstimationAddressId = chassisEstimationAddressId;
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
    }

    // 생성
    public static ChassisEstimationInfoEntity of(
            final Long userId,
            final Long chassisEstimationAddressId,
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

        return new ChassisEstimationInfoEntity(
                null,
                userId,
                chassisEstimationAddressId,
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
                price
        );
    }
}
