package kr.hoppang.application.command.chassis.commandresults;

import java.util.List;
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
        // int etcFee, // 기타 비용 (배송비, 도수운반비)
        int customerFloor,
        int wholeCalculatedFee // 총 비용
) {

    @Getter
    public static class ChassisPriceResult {

        private final String chassisType;

        private final int width;

        private final int height;

        private int price;

        private Long chassisDiscountEventId;

        private Integer discountedPrice;

        public ChassisPriceResult(
                final String chassisType,
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

        public void setDiscount(final Long chassisDiscountEventId, final Integer discountedPrice) {
            this.chassisDiscountEventId = chassisDiscountEventId;
            this.discountedPrice = discountedPrice;
        }
    }
}
