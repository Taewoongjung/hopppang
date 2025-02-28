package kr.hoppang.adapter.inbound.chassis.webdto;

import static kr.hoppang.util.calculator.ChassisPriceCalculator.calculateSurtax;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import lombok.AccessLevel;
import lombok.Builder;

public record GetEstimatedChassisByIdWebDtoV1() {

    @Builder(access = AccessLevel.PRIVATE)
    public record Res(
            long estimationId,

            String company,

            List<ChassisPrice> chassisPriceResultList,

            int deliveryFee,

            int demolitionFee,

            int maintenanceFee,

            int ladderFee,

            int freightTransportFee,

            int customerFloor,

            int wholeCalculatedFee,

            int surtax,

            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdAt
    ) {

        @Builder(access = AccessLevel.PRIVATE)
        private record ChassisPrice(String chassisType, int width, int height, int price) { }

        public static Res of(
                final ChassisEstimationInfo estimationInfo) {

            List<ChassisPrice> chassisPriceResultList = new ArrayList<>();

            estimationInfo.getChassisEstimationSizeInfoList()
                    .forEach(e ->
                            chassisPriceResultList.add(
                                    ChassisPrice.builder()
                                            .chassisType(e.getChassisType().getChassisName())
                                            .width(e.getWidth())
                                            .height(e.getHeight())
                                            .price(e.getPrice())
                                            .build()
                            ));

            return Res.builder()
                    .estimationId(estimationInfo.getId())
                    .company(estimationInfo.getCompanyType().getCompanyName())
                    .chassisPriceResultList(chassisPriceResultList)
                    .deliveryFee(estimationInfo.getDeliveryFee())
                    .demolitionFee(estimationInfo.getDemolitionFee())
                    .maintenanceFee(estimationInfo.getMaintenanceFee())
                    .ladderFee(estimationInfo.getLadderCarFee())
                    .freightTransportFee(estimationInfo.getFreightTransportFee())
                    .customerFloor(estimationInfo.getCustomerLivingFloor())
                    .wholeCalculatedFee(estimationInfo.getTotalPrice())
                    .surtax(calculateSurtax(estimationInfo.getTotalPrice()))
                    .createdAt(estimationInfo.getCreatedAt())
                    .build();
        }
    }
}
