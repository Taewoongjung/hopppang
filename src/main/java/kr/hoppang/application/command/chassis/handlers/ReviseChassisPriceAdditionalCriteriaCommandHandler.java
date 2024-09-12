package kr.hoppang.application.command.chassis.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.chassis.commands.ReviseChassisPriceAdditionalCriteriaCommand;
import kr.hoppang.application.command.chassis.commands.ReviseChassisPriceAdditionalCriteriaCommand.ReviseChassisPriceAdditionalCriteria;
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

        List<AdditionalChassisPriceCriteria> beforeModificationInfos = additionalChassisPriceCriteriaRepository.findAll();

        List<AdditionalChassisPriceCriteria> reviseRepositoryDto = new ArrayList<>();

        for (ReviseChassisPriceAdditionalCriteria revisingTarget : command.reqList()) {
            beforeModificationInfos.stream()
                    .filter(f -> f.getType().equals(revisingTarget.type()))
                    .filter(f2 -> !f2.comparePriceWithTarget(revisingTarget.revisingPrice()))
                    .forEach(revisingObj -> {
                        revisingObj.revisePrice(revisingTarget.revisingPrice());
                        reviseRepositoryDto.add(revisingObj);
                    });
        }

        return additionalChassisPriceCriteriaRepository.reviseAdditionalChassisPriceCriteria(reviseRepositoryDto);
    }
}
