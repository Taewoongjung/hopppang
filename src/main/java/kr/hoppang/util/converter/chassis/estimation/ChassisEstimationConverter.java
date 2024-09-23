package kr.hoppang.util.converter.chassis.estimation;

import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationAddressEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;

public class ChassisEstimationConverter {

    public static ChassisEstimationInfoEntity chassisEstimationInfoToEntity(
            final ChassisEstimationInfo pojo) {

        return ChassisEstimationInfoEntity.of(
                pojo.getUserId(),
                pojo.getChassisEstimationAddress() != null ? pojo.getChassisEstimationAddress().getId() : null,
                pojo.getCompanyType(),
                pojo.getLaborFee(),
                pojo.getLadderCarFee(),
                pojo.getDemolitionFee(),
                pojo.getMaintenanceFee(),
                pojo.getFreightTransportFee(),
                pojo.getDeliveryFee(),
                pojo.getPrice());
    }

    public static ChassisEstimationAddressEntity chassisEstimationAddressToEntity(
            final ChassisEstimationAddress pojo) {

        return ChassisEstimationAddressEntity.of(
                pojo.getAddress(),
                pojo.getSubAddress(),
                pojo.getBuildingNumber()
        );
    }
}
