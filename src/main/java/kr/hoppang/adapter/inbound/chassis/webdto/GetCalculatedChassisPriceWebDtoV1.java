package kr.hoppang.adapter.inbound.chassis.webdto;

import static kr.hoppang.adapter.common.exception.ErrorType.CHASSIS_TYPE_IS_MANDATORY;
import static kr.hoppang.adapter.common.exception.ErrorType.COMPANY_TYPE_IS_MANDATORY;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.application.command.chassis.commandresults.CalculateChassisPriceCommandHandlerCommandResult;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class GetCalculatedChassisPriceWebDtoV1 {

    public record Req(String zipCode,
                      String address,
                      String subAddress,
                      String buildingNumber,
                      List<ReqCalculateChassisPrice> reqCalculateChassisPriceList) {

        public void validate() {
            this.reqCalculateChassisPriceList.forEach(ReqCalculateChassisPrice::checkIfNull);
        }

        public CalculateChassisPriceCommand toQuery() {
            List<CalculateChassisPrice> queryList = new ArrayList<>();

            this.reqCalculateChassisPriceList.forEach(e -> {
                queryList.add(new CalculateChassisPrice(
                        e.chassisType,
                        e.companyType,
                        e.width,
                        e.height
                ));
            });

            return new CalculateChassisPriceCommand(
                    forTestPeriodAddressZipCode(this.zipCode),
                    forTestPeriodAddressInput(this.address),
                    forTestPeriodAddressInput(this.subAddress),
                    forTestPeriodAddressInput(this.buildingNumber),
                    queryList,
                    reqCalculateChassisPriceList.get(0).floorCustomerLiving,
                    reqCalculateChassisPriceList.get(0).isScheduledForDemolition,
                    reqCalculateChassisPriceList.get(0).isResident
            );
        }
    }
    private static String forTestPeriodAddressZipCode(final String target) {
        if (target == null || "".equals(target)) {
            return "00000";
        }
        return target;
    }

    private static String forTestPeriodAddressInput(final String target) {
        if (target == null || "".equals(target)) {
            return "테스트 기간";
        }
        return target;
    }

    @Getter
    @AllArgsConstructor
    private static class ReqCalculateChassisPrice {
        private ChassisType chassisType;
        private CompanyType companyType;
        private int width;
        private int height;
        private int floorCustomerLiving;
        private boolean isScheduledForDemolition;
        private boolean isResident;

        protected void checkIfNull() {
            check(companyType == null, COMPANY_TYPE_IS_MANDATORY);
            check(chassisType == null, CHASSIS_TYPE_IS_MANDATORY);
        }
    }


    public record Res(
            String company,
            List<ChassisPrice> chassisPriceResultList,
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
        private record ChassisPrice(String chassisType, int width, int height, int price) { }

        public static Res of(final CalculateChassisPriceCommandHandlerCommandResult commandResult) {

            List<ChassisPrice> chassisPriceResultList = new ArrayList<>();

            commandResult.chassisPriceResultList()
                    .forEach(e ->
                            chassisPriceResultList.add(
                                    new ChassisPrice(
                                            e.chassisType(),
                                            e.width(), e.height(),
                                            e.price()
                                    )
                            ));

            return new Res(
                    commandResult.company(),
                    chassisPriceResultList,
                    commandResult.deliveryFee(),
                    commandResult.demolitionFee(),
                    commandResult.maintenanceFee(),
                    commandResult.laborFee(),
                    commandResult.ladderFee(),
                    commandResult.freightTransportFee(),
                    commandResult.customerFloor(),
                    commandResult.wholeCalculatedFee()
            );
        }
    }
}
