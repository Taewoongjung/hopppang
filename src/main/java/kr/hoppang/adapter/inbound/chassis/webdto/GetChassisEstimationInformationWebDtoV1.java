package kr.hoppang.adapter.inbound.chassis.webdto;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;

public class GetChassisEstimationInformationWebDtoV1 {

    public record Res(Long id,
                        Long userId,
                        CompanyType companyType,
                        ChassisEstimationAddress chassisEstimationAddress,
                        int totalPrice,
                        LocalDateTime createdAt,
                        AdditionalChassisPriceInfo additionalChassisPriceInfo,
                        List<ChassisSize> chassisSizeList) {

        public record ChassisSize(String chassisType,
                                      int width,
                                      int height,
                                      int price) {

        }

        public record AdditionalChassisPriceInfo(int laborFee,
                                                 int ladderCarFee,
                                                 int demolitionFee,
                                                 int maintenanceFee,
                                                 int freightTransportFee,
                                                 int deliveryFee) {

        }
    }
}
