package kr.hoppang.application.command.chassis.handlers;

import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.chassis.commands.ReviseChassisPriceAdditionalCriteriaCommand;
import kr.hoppang.domain.chassis.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviseChassisPriceAdditionalCriteriaCommandHandler
        implements ICommandHandler<ReviseChassisPriceAdditionalCriteriaCommand, Boolean> {

    private final AdditionalChassisPriceCriteriaRepository additionalChassisPriceCriteriaRepository;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Boolean handle(final ReviseChassisPriceAdditionalCriteriaCommand command) {

        return additionalChassisPriceCriteriaRepository.reviseAdditionalChassisPriceCriteria(
                command.reqList().stream()
                        .map(e -> AdditionalChassisPriceCriteria.of(e.type(), e.revisingPrice()))
                        .collect(Collectors.toList()));
    }
}
