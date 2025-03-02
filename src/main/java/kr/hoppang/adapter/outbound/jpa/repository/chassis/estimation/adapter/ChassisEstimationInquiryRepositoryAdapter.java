package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.adapter;

import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInquiryEntity;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.ChassisEstimationInquiryJpaRepository;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ChassisEstimationInquiryRepositoryAdapter implements
        ChassisEstimationInquiryRepository {

    private final ChassisEstimationInquiryJpaRepository chassisEstimationInquiryJpaRepository;


    @Override
    public void create(final long userId, final long estimationId, final String strategy) {

        chassisEstimationInquiryJpaRepository.save(
                ChassisEstimationInquiryEntity.of(
                        userId,
                        estimationId,
                        strategy
                )
        );
    }
}
