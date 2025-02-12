package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto;

import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.CompanyType;

public record FindChassisEstimationInfoByUserIdRepositoryDto() {

    public record Response(
            Long id,
            Long userId,
            CompanyType companyType,
            int ladderCarFee,
            int laborFee,
            int demolitionFee,
            int maintenanceFee,
            int freightTransportFee,
            int deliveryFee,
            int appliedIncrementRate,
            int totalPrice,
            int customerLivingFloor,
            LocalDateTime createdAt
    ) { }
}
