package kr.hoppang.application.readmodel.chassis.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.adapter.outbound.alarm.dto.RequestEstimationInquiry;
import kr.hoppang.application.readmodel.chassis.queries.InquiryChassisEstimationCommand;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryChassisEstimation implements IQueryHandler<InquiryChassisEstimationCommand, Boolean> {

    private final ApplicationEventPublisher eventPublisher;
    private final ChassisEstimationRepository chassisEstimationRepository;

    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Boolean handle(final InquiryChassisEstimationCommand query) {
        log.info("executed InquiryChassisEstimationCommand = {}", query);

        ChassisEstimationInfo chassisEstimationInfo = chassisEstimationRepository.findChassisEstimationInfoById(
                query.estimationId());

        eventPublisher.publishEvent(new RequestEstimationInquiry(chassisEstimationInfo));

        return true;
    }
}
