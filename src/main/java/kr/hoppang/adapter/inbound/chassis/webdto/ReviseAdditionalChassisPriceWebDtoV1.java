package kr.hoppang.adapter.inbound.chassis.webdto;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.application.command.chassis.commands.ReviseChassisPriceAdditionalCriteriaCommand;
import kr.hoppang.application.command.chassis.commands.ReviseChassisPriceAdditionalCriteriaCommand.ReviseChassisPriceAdditionalCriteria;

public class ReviseAdditionalChassisPriceWebDtoV1 {

    public record Req(List<ReviseAdditionalChassisPrice> reqReviseAdditionalChassisPriceList) {

        public record ReviseAdditionalChassisPrice( AdditionalChassisPriceCriteriaType type,
                                                    int revisingPrice) {

        }

        public ReviseChassisPriceAdditionalCriteriaCommand toCommand() {

            List<ReviseChassisPriceAdditionalCriteria> convertingList = new ArrayList<>();

            this.reqReviseAdditionalChassisPriceList.forEach(e -> {
                convertingList.add(new ReviseChassisPriceAdditionalCriteria(e.type, e.revisingPrice));
            });

            return new ReviseChassisPriceAdditionalCriteriaCommand(convertingList);
        }
    }
}
