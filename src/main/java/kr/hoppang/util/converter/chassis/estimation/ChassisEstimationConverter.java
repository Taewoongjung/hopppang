package kr.hoppang.util.converter.chassis.estimation;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationAddressEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationSizeInfoEntity;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInquiry;
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
                pojo.getAppliedIncrementRate(),
                pojo.getTotalPrice(),
                pojo.getCustomerLivingFloor());
    }

    public static List<ChassisEstimationSizeInfoEntity> chassisEstimationSizeInfoToEntity(
            final List<ChassisEstimationSizeInfo> pojoList, final Long chassisEstimationInfoId) {

        List<ChassisEstimationSizeInfoEntity> entityList = new ArrayList<>();

        pojoList.forEach(e -> entityList.add(
                ChassisEstimationSizeInfoEntity.of(
                        chassisEstimationInfoId,
                        e.getChassisType(),
                        e.getWidth(),
                        e.getHeight(),
                        e.getPrice(),
                        e.getChassisDiscountEventId(),
                        e.getDiscountedPrice()
                ))
        );

        return entityList;
    }

    public static ChassisEstimationAddressEntity chassisEstimationAddressToEntity(
            final ChassisEstimationAddress pojo) {

        return ChassisEstimationAddressEntity.of(
                pojo.getChassisEstimationInfoId(),
                pojo.getZipCode(),
                pojo.getState(),
                pojo.getCity(),
                pojo.getTown(),
                pojo.getBCode(),
                pojo.getRemainAddress(),
                pojo.getBuildingNumber(),
                pojo.getIsApartment(),
                pojo.getIsExpanded()
        );
    }

    public static ChassisEstimationInquiry chassisEstimationInquiryToEntity(
            final ChassisEstimationInquiry pojo
    ) {

        return ChassisEstimationInquiry.of(
                pojo.getId(),
                pojo.getUserId(),
                pojo.getChassisEstimationInfoId(),
                pojo.getStrategy(),
                pojo.getCreatedAt()
        );
    }
}
