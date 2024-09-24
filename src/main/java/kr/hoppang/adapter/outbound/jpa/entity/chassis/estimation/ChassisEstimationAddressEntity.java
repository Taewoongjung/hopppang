package kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String zipCode;
    private String address;
    private String subAddress;
    private String buildingNumber;

    private ChassisEstimationAddressEntity(
            final Long id,
            final String zipCode,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        this.id = id;
        this.zipCode = zipCode;
        this.address = address;
        this.subAddress = subAddress;
        this.buildingNumber = buildingNumber;
    }

    public static ChassisEstimationAddressEntity of(
            final String zipCode,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new ChassisEstimationAddressEntity(null, zipCode, address, subAddress, buildingNumber);
    }
}
