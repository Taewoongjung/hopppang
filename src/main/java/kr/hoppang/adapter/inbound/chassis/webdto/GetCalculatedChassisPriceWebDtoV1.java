package kr.hoppang.adapter.inbound.chassis.webdto;

import static kr.hoppang.adapter.common.exception.ErrorType.CHASSIS_TYPE_IS_MANDATORY;
import static kr.hoppang.adapter.common.exception.ErrorType.COMPANY_TYPE_IS_MANDATORY;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class GetCalculatedChassisPriceWebDtoV1 {

    public record Req(List<ReqCalculateChassisPrice> reqCalculateChassisPriceList) {

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
                        e.height,
                        e.floorCustomerLiving,
                        e.isScheduledForDemolition,
                        e.isResident
                ));
            });

            return new CalculateChassisPriceCommand(queryList);
        }
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
}
