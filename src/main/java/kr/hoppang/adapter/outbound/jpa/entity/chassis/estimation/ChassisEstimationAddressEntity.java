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
    private String address;
    private String subAddress;
    private String buildingNumber;

    private ChassisEstimationAddressEntity(
            final Long id,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        this.id = id;
        this.address = address;
        this.subAddress = subAddress;
        this.buildingNumber = buildingNumber;
    }

    public static ChassisEstimationAddressEntity of(
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new ChassisEstimationAddressEntity(null, address, subAddress, buildingNumber);
    }
}