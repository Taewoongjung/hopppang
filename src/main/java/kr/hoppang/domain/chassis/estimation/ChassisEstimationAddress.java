package kr.hoppang.domain.chassis.estimation;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChassisEstimationAddress {

    private Long id;
    private Long chassisEstimationInfoId;
    private final String zipCode;
    private final String address;
    private final String subAddress;
    private final String buildingNumber;

    private ChassisEstimationAddress(
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

    public static ChassisEstimationAddress of(
            final String zipCode,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new ChassisEstimationAddress(
                null, null, zipCode, address, subAddress, buildingNumber);
    }

    public static ChassisEstimationAddress of(
            final Long id,
            final Long chassisEstimationInfoId,
            final String zipCode,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new ChassisEstimationAddress(
                id, chassisEstimationInfoId, zipCode, address, subAddress, buildingNumber);
    }

    public void setChassisEstimationInfoId(final Long id) {
        this.chassisEstimationInfoId = id;
    }
}
