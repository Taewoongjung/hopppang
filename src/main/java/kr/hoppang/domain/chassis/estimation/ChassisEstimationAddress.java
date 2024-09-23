package kr.hoppang.domain.chassis.estimation;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChassisEstimationAddress {

    private Long id;
    private final String address;
    private final String subAddress;
    private final String buildingNumber;

    private ChassisEstimationAddress(
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

    public static ChassisEstimationAddress of(
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new ChassisEstimationAddress(null, address, subAddress, buildingNumber);
    }

    public static ChassisEstimationAddress of(
            final Long id,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new ChassisEstimationAddress(id, address, subAddress, buildingNumber);
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
