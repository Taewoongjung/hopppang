package kr.hoppang.adapter.outbound.alarm.dto;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;

public record NewEstimation(String userName,
                            String userAddress,
                            CompanyType company,
                            List<Estimation> estimationList) {

    public static NewEstimation of(
            final String userName,
            final String userAddress,
            final CompanyType company,
            final List<CalculateChassisPrice> calculateChassisList) {

        List<Estimation> estimationList = new ArrayList<>();

        calculateChassisList.forEach(e -> estimationList.add(
                new Estimation(
                        e.chassisType(),
                        e.width(), e.height()
                )));

        return new NewEstimation(userName, userAddress, company, estimationList);
    }

    public record Estimation(ChassisType chassisType,
                             int width,
                             int height) {
    }
}
