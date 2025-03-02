package kr.hoppang.application.command.chassis.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.alarm.dto.RequestEstimationInquiry;
import kr.hoppang.application.command.chassis.commands.InquiryChassisEstimationCommand;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationInquiryRepository;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryChassisEstimationCommandHandler implements
        ICommandHandler<InquiryChassisEstimationCommand, Boolean> {

    private final ApplicationEventPublisher eventPublisher;
    private final ChassisEstimationRepository chassisEstimationRepository;
    private final ChassisEstimationInquiryRepository chassisEstimationInquiryRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean handle(final InquiryChassisEstimationCommand query) {
        log.info("executed InquiryChassisEstimationCommand = {}", query);

        ChassisEstimationInfo chassisEstimationInfo = chassisEstimationRepository.findChassisEstimationInfoById(
                query.estimationId(), false);

        chassisEstimationInquiryRepository.create(
                query.userId(),
                query.estimationId(),
                query.strategy()
        );

        eventPublisher.publishEvent(
                new RequestEstimationInquiry(
                        chassisEstimationInfo,
                        query.strategy()
                )
        );

        log.info("executed InquiryChassisEstimationCommand successfully");
        return true;
    }
}
