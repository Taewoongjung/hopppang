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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private int appliedIncrementRate;

    private int totalPrice;

    private int customerLivingFloor;

    private Long chassisDiscountEventId;

    private Integer discountedTotalPrice;

    @Setter
    @OneToOne(mappedBy = "chassisEstimationInfo", cascade = CascadeType.ALL)
    private ChassisEstimationAddressEntity chassisEstimationAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chassisEstimationInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChassisEstimationSizeInfoEntity> chassisEstimationSizeInfoList = new ArrayList<>();

    @Builder
    private ChassisEstimationInfoEntity(
            final Long id,
            final Long userId,
            final CompanyType companyType,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int appliedIncrementRate,
            final int totalPrice,
            final int customerLivingFloor,
            final Long chassisDiscountEventId,
            final Integer discountedTotalPrice,
            final ChassisEstimationAddressEntity chassisEstimationAddress,
            final List<ChassisEstimationSizeInfoEntity> chassisEstimationSizeInfoList
        ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.userId = userId;
        this.companyType = companyType;
        this.laborFee = laborFee;
        this.ladderCarFee = ladderCarFee;
        this.demolitionFee = demolitionFee;
        this.maintenanceFee = maintenanceFee;
        this.freightTransportFee = freightTransportFee;
        this.deliveryFee = deliveryFee;
        this.appliedIncrementRate = appliedIncrementRate;
        this.totalPrice = totalPrice;
        this.customerLivingFloor = customerLivingFloor;
        this.chassisDiscountEventId = chassisDiscountEventId;
        this.discountedTotalPrice = discountedTotalPrice;
        this.chassisEstimationAddress = chassisEstimationAddress;
        this.chassisEstimationSizeInfoList = chassisEstimationSizeInfoList;
    }

    public static ChassisEstimationInfoEntity of(
            final Long userId,
            final CompanyType companyType,
            final int laborFee,
            final int ladderCarFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int freightTransportFee,
            final int deliveryFee,
            final int appliedIncrementRate,
            final int totalPrice,
            final int customerLivingFloor,
            final Long chassisDiscountEventId,
            final Integer discountedTotalPrice
    ) {

        return new ChassisEstimationInfoEntity(
                null,
                userId,
                companyType,
                laborFee,
                ladderCarFee,
                demolitionFee,
                maintenanceFee,
                freightTransportFee,
                deliveryFee,
                appliedIncrementRate,
                totalPrice,
                customerLivingFloor,
                chassisDiscountEventId,
                discountedTotalPrice,
                null, null
        );
    }

    public ChassisEstimationInfo toPojo() {
        return ChassisEstimationInfo.of(
                getId(),
                getUserId(),
                getChassisEstimationAddress() != null ?
                        getChassisEstimationAddress().toPojo() : null,
                getCompanyType(),
                getLaborFee(),
                getLadderCarFee(),
                getDemolitionFee(),
                getMaintenanceFee(),
                getFreightTransportFee(),
                getDeliveryFee(),
                getAppliedIncrementRate(),
                getTotalPrice(),
                getCustomerLivingFloor(),
                getChassisDiscountEventId(),
                getDiscountedTotalPrice(),
                getCreatedAt(),
                getLastModified()
        );
    }

    public ChassisEstimationInfo toPojoWithAllRelations() {
        return ChassisEstimationInfo.of(
                getId(),
                getUserId(),
                getChassisEstimationAddress() != null ?
                        getChassisEstimationAddress().toPojo() : null,
                getCompanyType(),
                getLaborFee(),
                getLadderCarFee(),
                getDemolitionFee(),
                getMaintenanceFee(),
                getFreightTransportFee(),
                getDeliveryFee(),
                getAppliedIncrementRate(),
                getTotalPrice(),
                getCustomerLivingFloor(),
                getChassisDiscountEventId(),
                getDiscountedTotalPrice(),
                getChassisEstimationSizeInfoList() != null ?
                        getChassisEstimationSizeInfoList().stream()
                                        .map(ChassisEstimationSizeInfoEntity::toPojo)
                                                .toList() : null,
                getCreatedAt(),
                getLastModified()
        );
    }
}
