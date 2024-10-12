package kr.hoppang.application.command.chassis.handlers;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.chassis.commands.ReviseChassisPriceAdditionalCriteriaCommand;
import kr.hoppang.application.command.chassis.commands.ReviseChassisPriceAdditionalCriteriaCommand.ReviseChassisPriceAdditionalCriteria;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.price.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
    @CacheEvict(value = "additionalChassisPriceCriteria", key = "additionalChassisPriceCriteria", allEntries = true)
    public Boolean handle(final ReviseChassisPriceAdditionalCriteriaCommand event) {

        log.info("[핸들러 - 부가 가격 정보 수정] ReviseChassisPriceAdditionalCriteriaCommand = {}", event);

        List<AdditionalChassisPriceCriteria> beforeModificationInfos = additionalChassisPriceCriteriaRepository.findAll();

        List<AdditionalChassisPriceCriteria> reviseRepositoryDto = new ArrayList<>();

        for (ReviseChassisPriceAdditionalCriteria revisingTarget : event.reqList()) {
            beforeModificationInfos.stream()
                    .filter(f -> f.getType().equals(revisingTarget.type()))
                    .filter(f2 -> !f2.comparePriceWithTarget(revisingTarget.revisingPrice()))
                    .forEach(revisingObj -> {
                        revisingObj.revisePrice(revisingTarget.revisingPrice());
                        reviseRepositoryDto.add(revisingObj);
                    });
        }

        boolean isSuccess = additionalChassisPriceCriteriaRepository
                .reviseAdditionalChassisPriceCriteria(reviseRepositoryDto);

        log.info("[핸들러 - 부가 가격 정보 수정] 성공");

        return isSuccess;
    }
}
