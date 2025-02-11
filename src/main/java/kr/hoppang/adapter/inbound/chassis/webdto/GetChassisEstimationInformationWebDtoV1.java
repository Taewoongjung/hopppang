package kr.hoppang.adapter.inbound.chassis.webdto;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.domain.chassis.CompanyType;

public class GetChassisEstimationInformationWebDtoV1 {

    public record Res(Long id,
                        Long userId,
                        String userName,
                        String userEmail,
                        String userPhoneNumber,
                        CompanyType companyType,
                        EstimationAddress chassisEstimationAddress,
                        int totalPrice,
                        LocalDateTime createdAt,
                        AdditionalChassisPriceInfo additionalChassisPriceInfo,
                        List<ChassisSize> chassisSizeList) {

        public record EstimationAddress(String zipCode,
                                        String address,
                                        String subAddress,
                                        boolean isApartment,
                                        boolean isExpanded) {

        }

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
