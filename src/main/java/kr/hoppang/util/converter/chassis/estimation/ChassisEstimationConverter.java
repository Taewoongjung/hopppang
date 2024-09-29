package kr.hoppang.util.converter.chassis.estimation;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationAddressEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationSizeInfoEntity;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;

public class ChassisEstimationConverter {

    public static ChassisEstimationInfoEntity chassisEstimationInfoToEntity(
            final ChassisEstimationInfo pojo) {

        return ChassisEstimationInfoEntity.of(
                pojo.getUserId(),
                pojo.getCompanyType(),
                pojo.getLaborFee(),
                pojo.getLadderCarFee(),
                pojo.getDemolitionFee(),
                pojo.getMaintenanceFee(),
                pojo.getFreightTransportFee(),
                pojo.getDeliveryFee(),
                pojo.getTotalPrice());
    }

    public static List<ChassisEstimationSizeInfoEntity> chassisEstimationSizeInfoToEntity(
            final List<ChassisEstimationSizeInfo> pojoList, final Long chassisEstimationInfoId) {

        List<ChassisEstimationSizeInfoEntity> entityList = new ArrayList<>();

        pojoList.forEach(e -> entityList.add(ChassisEstimationSizeInfoEntity.of(
                chassisEstimationInfoId,
                e.getChassisType(),
                e.getWidth(),
                e.getHeight(),
                e.getPrice()
        )));

        return entityList;
    }

    public static ChassisEstimationAddressEntity chassisEstimationAddressToEntity(
            final ChassisEstimationAddress pojo) {

        return ChassisEstimationAddressEntity.of(
                pojo.getChassisEstimationInfoId(),
                pojo.getZipCode(),
                pojo.getAddress(),
                pojo.getSubAddress(),
                pojo.getBuildingNumber()
        );
    }
}
