package kr.hoppang.application.command.chassis.commandresults;

import java.util.List;
import kr.hoppang.domain.chassis.ChassisType;
import lombok.Getter;

public record CalculateChassisPriceCommandHandlerCommandResult(
        long estimationId,
        String company,
        List<ChassisPriceResult> chassisPriceResultList,
        int deliveryFee,
        int demolitionFee,
        int maintenanceFee,
        int ladderFee,
        int freightTransportFee,
        int customerFloor,
        int laborFee,
        int wholeCalculatedFee, // 총 비용
        Integer discountedWholeCalculatedFeeAmount, // 할인 된 비용
        Integer discountedWholeCalculatedFeeWithSurtax // 할인 된 총 비용
) {

    @Getter
    public static class ChassisPriceResult {

        private final ChassisType chassisType;

        private final int width;

        private final int height;

        private int price;

        private Long chassisDiscountEventId;

        private Integer discountedRate;

        private Integer discountedPrice;

        public ChassisPriceResult(
                final ChassisType chassisType,
                final int width,
                final int height,
                final int price
        ) {
            this.chassisType = chassisType;
            this.width = width;
            this.height = height;
            this.price = price;
        }

        public void addLaborFeeToChassisPrice(final int laborFee) {
            this.price += laborFee;
        }

        public void setDiscount(
                final Long chassisDiscountEventId,
                final Integer discountedRate,
                final Integer discountedPrice
        ) {
            this.chassisDiscountEventId = chassisDiscountEventId;
            this.discountedRate = discountedRate;
            this.discountedPrice = discountedPrice;
        }
    }
}
