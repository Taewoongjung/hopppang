package kr.hoppang.application.command.chassis.commands;

import java.util.List;
import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.user.User;

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
        boolean isResident,
        long userId

) implements ICommand {

        public record CalculateChassisPrice(
                ChassisType chassisType,
                CompanyType companyType,
                int width, int height) {

        }
}
