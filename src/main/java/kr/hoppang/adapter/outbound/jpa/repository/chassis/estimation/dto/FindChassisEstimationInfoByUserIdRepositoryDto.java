package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto.FindChassisEstimationInfoByUserIdRepositoryDto.Response.EstimationChassis.ChassisEstimatedAddress;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto.FindChassisEstimationInfoByUserIdRepositoryDto.Response.EstimationChassis.ChassisSizeInfo;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Slice;

public record FindChassisEstimationInfoByUserIdRepositoryDto() {

    @Builder
    public record Response(
            Slice<EstimationChassis> estimationChassisList,
            Long lastEstimationId
    ) {

        @Builder
        public record EstimationChassis(
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
                ChassisEstimatedAddress address,
                List<ChassisSizeInfo> chassisEstimationSizeInfoList
        ) {

            @Builder(access = AccessLevel.PRIVATE)
            public record ChassisSizeInfo(
                    ChassisType chassisType,
                    int width,
                    int height
            ) { }

            @Builder(access = AccessLevel.PRIVATE)
            public record ChassisEstimatedAddress(
                    String zipCode,
                    String state,
                    String city,
                    String town,
                    String remainAddress
            ) { }
        }

        public static EstimationChassis of(final ChassisEstimationInfoEntity chassisEstimation) {
            return EstimationChassis.builder()
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
                    .address(
                            ChassisEstimatedAddress.builder()
                                    .zipCode(chassisEstimation.getChassisEstimationAddress()
                                            .getZipCode())
                                    .state(chassisEstimation.getChassisEstimationAddress().getState())
                                    .city(chassisEstimation.getChassisEstimationAddress().getCity())
                                    .town(chassisEstimation.getChassisEstimationAddress().getTown())
                                    .remainAddress(chassisEstimation.getChassisEstimationAddress()
                                            .getRemainAddress())
                                    .build()
                    )
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
