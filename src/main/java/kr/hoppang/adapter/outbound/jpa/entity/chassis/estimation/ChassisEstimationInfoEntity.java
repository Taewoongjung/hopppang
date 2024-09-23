package kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "company_type", nullable = false, columnDefinition = "varchar(20)")
    private CompanyType companyType;

    private int laborFee;

    private int ladderCarFee;

    private int demolitionFee;

    private int maintenanceFee;

    private int freightTransportFee;

    private int deliveryFee;

    private int totalPrice;

    private Long chassisEstimationAddressId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chassisEstimationInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChassisEstimationSizeInfoEntity> chassisEstimationSizeInfoList = new ArrayList<>();


    private ChassisEstimationInfoEntity(
            final Long id,
            final Long userId,
            final Long chassisEstimationAddressId,
            final CompanyType companyType,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int totalPrice
        ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.userId = userId;
        this.chassisEstimationAddressId = chassisEstimationAddressId;
        this.companyType = companyType;
        this.laborFee = laborFee;
        this.ladderCarFee = ladderCarFee;
        this.demolitionFee = demolitionFee;
        this.maintenanceFee = maintenanceFee;
        this.freightTransportFee = freightTransportFee;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
    }

    // 생성
    public static ChassisEstimationInfoEntity of(
            final Long userId,
            final Long chassisEstimationAddressId,
            final CompanyType companyType,
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
