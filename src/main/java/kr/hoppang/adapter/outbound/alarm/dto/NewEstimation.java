package kr.hoppang.adapter.outbound.alarm.dto;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.user.UserAddress;

public record NewEstimation(String userName,
                            String chassisInstallationAddress,
                            CompanyType company,
                            List<Estimation> estimationList) {

    public static NewEstimation of(
            final String userName,
            final String zipCode,
            final String state,
            final String city,
            final String town,
            final String remainAddress,
            final CompanyType company,
            final List<CalculateChassisPrice> calculateChassisList) {

        List<Estimation> estimationList = new ArrayList<>();

        calculateChassisList.forEach(e -> estimationList.add(
                new Estimation(
                        e.chassisType(),
                        e.width(), e.height()
                )));

        String chassisInstallationAddress =
                "[" + zipCode + "] " + state + " " + city + " " + town + " " + remainAddress;

        return new NewEstimation(userName, chassisInstallationAddress, company, estimationList);
    }

    public record Estimation(ChassisType chassisType,
                             int width,
                             int height) {
    }
}
