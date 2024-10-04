package kr.hoppang.application.command.chassis.commands;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;
import kr.hoppang.util.common.BoolType;

public record AddChassisEstimationInfoCommand(ChassisEstimationCommand chassisEstimationCommand) implements ICommand {

    public record ChassisEstimationCommand(String zipCode,
                                           String state,
                                           String city,
                                           String town,
                                           String bCode,
                                           String remainAddress,
                                           String buildingNumber,
                                           BoolType isApartment,
                                           BoolType isExpanded,
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
                        command.state,
                        command.city,
                        command.town,
                        command.bCode,
                        command.remainAddress,
                        command.buildingNumber,
                        command.isApartment,
                        command.isExpanded
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

        List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList = new ArrayList<>();

        this.chassisEstimationCommand.chassisSizeList.forEach(e -> {
            chassisEstimationSizeInfoList.add(
                    ChassisEstimationSizeInfo.of(
                            ChassisType.from(e.chassisType), e.width, e.height, e.price)
            );
        });

        return chassisEstimationSizeInfoList;
    }
}
