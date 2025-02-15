package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.Builder;

public record FindChassisEstimationInfoByUserIdRepositoryDto() {

    @Builder
    public record Response(
            Long id,
            Long userId,
            CompanyType companyType,
            int ladderCarFee,
            int laborFee,
            int demolitionFee,
            int maintenanceFee,
            int freightTransportFee,
            int deliveryFee,
            int appliedIncrementRate,
            int totalPrice,
            int customerLivingFloor,
            LocalDateTime createdAt,
            List<ChassisSizeInfo> chassisEstimationSizeInfoList
    ) {

        @Builder
        public record ChassisSizeInfo(
                ChassisType chassisType,
                int width,
                int height
        ) { }

        public static Response of(final ChassisEstimationInfoEntity chassisEstimation) {
            return Response.builder()
                    .id(chassisEstimation.getId())
                    .userId(chassisEstimation.getUserId())
                    .companyType(chassisEstimation.getCompanyType())
                    .ladderCarFee(chassisEstimation.getLadderCarFee())
                    .laborFee(chassisEstimation.getLaborFee())
                    .demolitionFee(chassisEstimation.getDemolitionFee())
                    .maintenanceFee(chassisEstimation.getMaintenanceFee())
                    .freightTransportFee(chassisEstimation.getFreightTransportFee())
                    .deliveryFee(chassisEstimation.getDeliveryFee())
                    .appliedIncrementRate(chassisEstimation.getAppliedIncrementRate())
                    .totalPrice(chassisEstimation.getTotalPrice())
                    .customerLivingFloor(chassisEstimation.getCustomerLivingFloor())
                    .createdAt(chassisEstimation.getCreatedAt())
                    .chassisEstimationSizeInfoList(
                            chassisEstimation.getChassisEstimationSizeInfoList().stream()
                                    .map(sizeInfo ->
                                            ChassisSizeInfo.builder()
                                                    .chassisType(sizeInfo.getChassisType())
                                                    .width(sizeInfo.getWidth())
                                                    .height(sizeInfo.getHeight())
                                                    .build()
                                    ).toList()
                    )
                    .build();
        }
    }
}
