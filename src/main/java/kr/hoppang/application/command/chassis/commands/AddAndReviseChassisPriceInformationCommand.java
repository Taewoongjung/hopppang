package kr.hoppang.application.command.chassis.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public record AddAndReviseChassisPriceInformationCommand(
        CompanyType companyType,
        ChassisType chassisType,
        int width,
        int height,
        int price) implements ICommand {

}
