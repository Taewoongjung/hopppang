package kr.hoppang.application.command.chassis.commandresults;

import java.util.List;

public record CalculateChassisPriceCommandHandlerCommandResult(
        String company,
        String chassisType,
        List<ChassisPriceResult> chassisPriceResultList,
        int deliveryFee,
        int demolitionFee,
        int maintenanceFee,
        int ladderFee,
        // int etcFee, // 기타 비용 (배송비, 도수운반비)
        int wholeCalculatedFee // 총 비용
        ) {

    public record ChassisPriceResult(int width,
                                     int height,
                                     int price) { }

//    public int calculateEtcFee(final int targetFloor) {
//
//    }
}
