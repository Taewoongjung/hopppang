package kr.hoppang.application.command.chassis.commands;

import java.util.List;
import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.price.pricecriteria.AdditionalChassisPriceCriteriaType;

public record ReviseChassisPriceAdditionalCriteriaCommand(
        List<ReviseChassisPriceAdditionalCriteria> reqList
) implements ICommand {

    public record ReviseChassisPriceAdditionalCriteria(
            AdditionalChassisPriceCriteriaType type,
            int revisingPrice
    ) {

    }
}
