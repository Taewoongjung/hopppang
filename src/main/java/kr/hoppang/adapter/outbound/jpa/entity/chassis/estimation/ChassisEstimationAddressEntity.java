package kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    private String zipCode;
    private String address;
    private String subAddress;
    private String buildingNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chassis_estimation_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ChassisEstimationInfoEntity chassisEstimationInfo;

    private ChassisEstimationAddressEntity(
            final Long id,
            final Long chassisEstimationInfoId,
            final String zipCode,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        this.id = id;
        this.chassisEstimationInfoId = chassisEstimationInfoId;
        this.zipCode = zipCode;
        this.address = address;
        this.subAddress = subAddress;
        this.buildingNumber = buildingNumber;
    }

    public static ChassisEstimationAddressEntity of(
            final Long chassisEstimationInfoId,
            final String zipCode,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new ChassisEstimationAddressEntity(null, chassisEstimationInfoId, zipCode, address, subAddress, buildingNumber);
    }
}
