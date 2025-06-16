package kr.hoppang.adapter.inbound.chassis.webdto;

import static kr.hoppang.adapter.common.exception.ErrorType.COMPANY_TYPE_IS_MANDATORY;
import static kr.hoppang.adapter.common.exception.ErrorType.CHASSIS_TYPE_IS_MANDATORY;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class GetCalculatedChassisPriceWebDtoV1 {

    public record Req(
            String zipCode,

            String state,

            String city,

            String town,

            String bCode,

            String remainAddress,

            String buildingNumber,

            boolean isApartment,

            boolean isExpanded,

            @NotNull
            List<ReqCalculateChassisPrice> reqCalculateChassisPriceList
    ) {

        public void validate() {
            this.reqCalculateChassisPriceList.forEach(ReqCalculateChassisPrice::checkIfNull);
        }

        public CalculateChassisPriceCommand toQuery(final Long userId) {
            List<CalculateChassisPrice> queryList = new ArrayList<>();

            this.reqCalculateChassisPriceList.forEach(
                    e -> {
                        queryList.add(
                                new CalculateChassisPrice(
                                        e.chassisType,
                                        e.companyType,
                                        e.width,
                                        e.height
                                )
                        );
            });

            return new CalculateChassisPriceCommand(
                    forTestPeriodAddressZipCode(this.zipCode),
                    forTestPeriodAddressInput(this.state),
                    forTestPeriodAddressInput(this.city),
                    forTestPeriodAddressInput(this.town),
                    forTestPeriodAddressInput(this.bCode),
                    forTestPeriodAddressInput(this.remainAddress),
                    forTestPeriodAddressInput(this.buildingNumber),
                    this.isApartment,
                    this.isExpanded,
                    queryList,
                    reqCalculateChassisPriceList.getFirst().floorCustomerLiving,
                    reqCalculateChassisPriceList.getFirst().isScheduledForDemolition,
                    reqCalculateChassisPriceList.getFirst().isResident,
                    userId
            );
        }
    }

    private static String forTestPeriodAddressZipCode(final String target) {
        if (target == null || target.isEmpty()) {
            return "00000";
        }
        return target;
    }

    private static String forTestPeriodAddressInput(final String target) {
        if (target == null || target.isEmpty()) {
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


    @Builder
    public record Res(
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

            int wholeCalculatedFee,

            int surtax,

            Integer discountedWholeCalculatedFeeAmount, // 할인 된 비용

            Integer discountedWholeCalculatedFeeWithSurtax // 할인 된 총 비용 discountedWholeCalculatedFee -> discountedWholeCalculatedFeeWithSurtax
    ) {

        @Builder
        @JsonInclude(Include.NON_NULL)
        public record ChassisPriceResult(
                String chassisType,
                int width,
                int height,
                int price,
                Integer discountedRate,
                Integer discountedPrice
        ) { }
    }
}
