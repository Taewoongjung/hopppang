package kr.hoppang.application.command.chassis.commands;

import java.util.List;
import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public record CalculateChassisPriceCommand(
        String zipCode,
        String state,
        String city,
        String town,
        String bCode,
        String remainAddress,
        String buildingNumber,
        boolean isApartment,
        boolean isExpanded,
        List<CalculateChassisPrice> calculateChassisPriceList,
        int floorCustomerLiving,
        boolean isScheduledForDemolition,
        boolean isResident

) implements ICommand {

        public record CalculateChassisPrice(
                ChassisType chassisType,
                CompanyType companyType,
                int width, int height) {

        }
}
