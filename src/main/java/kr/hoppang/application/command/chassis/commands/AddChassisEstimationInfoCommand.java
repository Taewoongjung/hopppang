package kr.hoppang.application.command.chassis.commands;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;

public record AddChassisEstimationInfoCommand(ChassisEstimationCommand chassisEstimationCommand) implements ICommand {

    public record ChassisEstimationCommand(String address,
                                           String zipCode,
                                           String subAddress,
                                           String buildingNumber,
                                           String company,
                                           int deliveryFee,
                                           int demolitionFee,
                                           int maintenanceFee,
                                           int laborFee,
                                           int ladderFee,
                                           int freightTransportFee,
                                           int customerFloor,
                                           int wholeCalculatedFee,
                                           List<ChassisSize> chassisSizeList

    ) {

        public record ChassisSize(String chassisType,
                           int width,
                           int height,
                           int price) { }
    }

    public ChassisEstimationInfo makeChassisEstimationInfo() {

        ChassisEstimationCommand command = this.chassisEstimationCommand;

        return ChassisEstimationInfo.of(
                null,
                ChassisEstimationAddress.of(
                        command.zipCode,
                        command.address,
                        command.subAddress,
                        command.buildingNumber
                ),
                CompanyType.from(command.company),
                command.laborFee,
                command.ladderFee,
                command.demolitionFee,
                command.maintenanceFee,
                command.freightTransportFee,
                command.deliveryFee,
                command.wholeCalculatedFee
        );
    }

    public List<ChassisEstimationSizeInfo> makeChassisEstimationSizeInfo() {

        ChassisEstimationCommand command = this.chassisEstimationCommand;

        List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList = new ArrayList<>();

        command.chassisSizeList.forEach(e -> {
            chassisEstimationSizeInfoList.add(
                    ChassisEstimationSizeInfo.of(
                            ChassisType.from(e.chassisType), e.width, e.height, e.price)
            );
        });

        return chassisEstimationSizeInfoList;
    }
}
