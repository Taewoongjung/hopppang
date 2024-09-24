package kr.hoppang.application.command.chassis.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.chassis.commands.AddChassisEstimationInfoCommand;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddChassisEstimationInfoCommandHandler implements ICommandHandler<AddChassisEstimationInfoCommand, Boolean> {

    private final ChassisEstimationRepository chassisEstimationRepository;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public Boolean handle(final AddChassisEstimationInfoCommand command) {
        log.info("[핸들러] AddChassisEstimationInfoCommand 수행");

        chassisEstimationRepository.registerChassisEstimation(command.makeChassisEstimationInfo(),
                command.makeChassisEstimationSizeInfo());

        log.info("[샤시 견적] 성공");
        return true;
    }
}
