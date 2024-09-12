package kr.hoppang.application.command.chassis.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public record CalculateChassisPriceCommand(
        ChassisType chassisType,
        CompanyType companyType,
        int width,
        int height,
        int floorCustomerLiving,
        boolean isScheduledForDemolition,
        boolean isResident
        ) implements ICommand {

}
