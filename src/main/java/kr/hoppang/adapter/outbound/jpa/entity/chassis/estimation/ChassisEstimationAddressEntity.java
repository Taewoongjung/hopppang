package kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "chassis_estimation_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChassisEstimationAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chassis_estimation_info_id", nullable = false, columnDefinition = "bigint")
    private Long chassisEstimationInfoId;

    @Column(name = "zip_code", columnDefinition = "char(5)")
    private String zipCode;

    private String state;

    private String city;

    private String town;

    @Column(name = "b_code", columnDefinition = "char(10)")
    private String bCode;

    private String remainAddress;

    private String buildingNumber;

    @Column(name = "is_apartment", columnDefinition = "char(1)")
    @Enumerated(value = EnumType.STRING)
    private BoolType isApartment;

    @Column(name = "is_expanded", columnDefinition = "char(1)")
    @Enumerated(value = EnumType.STRING)
    private BoolType isExpanded;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chassis_estimation_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ChassisEstimationInfoEntity chassisEstimationInfo;

    private ChassisEstimationAddressEntity(
            final Long id,
            final Long chassisEstimationInfoId,
            final String zipCode,
            final String state,
            final String city,
            final String town,
            final String bCode,
            final String remainAddress,
            final String buildingNumber,
            final BoolType isApartment,
            final BoolType isExpanded
    ) {
        this.id = id;
        this.chassisEstimationInfoId = chassisEstimationInfoId;
        this.zipCode = zipCode;
        this.state = state;
        this.city = city;
        this.town = town;
        this.bCode = bCode;
        this.remainAddress = remainAddress;
        this.buildingNumber = buildingNumber;
        this.isApartment = isApartment;
        this.isExpanded = isExpanded;
    }

    public static ChassisEstimationAddressEntity of(
            final Long chassisEstimationInfoId,
            final String zipCode,
            final String state,
            final String city,
            final String town,
            final String bCode,
            final String remainAddress,
            final String buildingNumber,
            final BoolType isApartment,
            final BoolType isExpanded
    ) {
        return new ChassisEstimationAddressEntity(
                null,
                chassisEstimationInfoId,
                zipCode, state, city, town, bCode, remainAddress, buildingNumber,
                isApartment, isExpanded);
    }
}
