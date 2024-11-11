package kr.hoppang.domain.chassis.estimation;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.hoppang.util.common.BoolType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChassisEstimationAddress {

    private Long id;
    private Long chassisEstimationInfoId;
    private final String zipCode;
    private String state;
    private String city;
    private String town;
    private String bCode;
    private String remainAddress;
    private String buildingNumber;
    private BoolType isApartment;
    private BoolType isExpanded;

    private ChassisEstimationAddress(
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

    public static ChassisEstimationAddress of(
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
        return new ChassisEstimationAddress(
                null, null,
                zipCode, state, city, town, bCode, remainAddress,
                buildingNumber, isApartment, isExpanded);
    }

    // 조회
    public static ChassisEstimationAddress of(
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
        return new ChassisEstimationAddress(
                id, chassisEstimationInfoId,
                zipCode, state, city, town, bCode, remainAddress,
                buildingNumber, isApartment, isExpanded);
    }

    public void setChassisEstimationInfoId(final Long id) {
        this.chassisEstimationInfoId = id;
    }
}
