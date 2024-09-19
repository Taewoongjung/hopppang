package kr.hoppang.application.command.chassis.commandresults;

import java.util.List;

public record CalculateChassisPriceCommandHandlerCommandResult(
        String company,
        List<ChassisPriceResult> chassisPriceResultList,
        int deliveryFee,
        int demolitionFee,
        int maintenanceFee,
        int laborFee,
        int ladderFee,
        int freightTransportFee,
        // int etcFee, // 기타 비용 (배송비, 도수운반비)
        int customerFloor,
        int wholeCalculatedFee // 총 비용
        ) {

    public record ChassisPriceResult(String chassisType,
                                     int width,
                                     int height,
                                     int price) { }

//    public int calculateEtcFee(final int targetFloor) {
//
//    }
}
