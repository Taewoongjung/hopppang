package kr.hoppang.application.command.chassis.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.chassis.commands.AddChassisEstimationInfoCommand;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddChassisEstimationInfoCommandHandler implements ICommandHandler<AddChassisEstimationInfoCommand, Long> {

    private final ChassisEstimationRepository chassisEstimationRepository;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long handle(final AddChassisEstimationInfoCommand command) {
        log.info("[핸들러 - 샤시 견적] AddChassisEstimationInfoCommand 수행");

        long registeredEstimationId = chassisEstimationRepository.registerChassisEstimation(
                command.makeChassisEstimationInfo(),
                command.makeChassisEstimationSizeInfo());

        log.info("[핸들러 - 샤시 견적] 성공");

        return registeredEstimationId;
    }
}
