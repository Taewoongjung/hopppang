package kr.hoppang.application.readmodel.chassis.queryresults;

import static kr.hoppang.util.common.BoolType.convertToBoolean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisEstimationInformationWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisEstimationInformationWebDtoV1.Res;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisEstimationInformationWebDtoV1.Res.AdditionalChassisPriceInfo;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisEstimationInformationWebDtoV1.Res.ChassisSize;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisEstimationInformationWebDtoV1.Res.EstimationAddress;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;

public record FindChassisEstimationInformationQueryHandlerResult(
        Long id,
        ChassisType chassisType,
        int width,
        int height,
        int price,
        Long userId,
        String userName,
        String userEmail,
        String userPhoneNumber,
        ChassisEstimationAddress chassisEstimationAddress,
        CompanyType companyType,
        int laborFee,
        int ladderCarFee,
        int demolitionFee,
        int maintenanceFee,
        int freightTransportFee,
        int deliveryFee,
        int totalPrice,
        LocalDateTime createdAt,
        LocalDateTime lastModified) {

    public static List<GetChassisEstimationInformationWebDtoV1.Res> toWebResponseObject(
            final List<FindChassisEstimationInformationQueryHandlerResult> target) {

        List<GetChassisEstimationInformationWebDtoV1.Res> resultList = new ArrayList<>();

        Set<Long> estimationIdList = target.stream().map(e -> e.id).collect(Collectors.toSet());

        for (Long estimationId : estimationIdList) {
            List<ChassisSize> chassisSizeListOfEstimation = new ArrayList<>();

            target.stream()
                    .filter(f -> estimationId.equals(f.id))
                    .forEach(e -> {
                        chassisSizeListOfEstimation.add(
                                new ChassisSize(e.chassisType.getChassisName(), e.width, e.height,
                                        e.price));
                    });

            target.stream()
                    .filter(f -> estimationId.equals(f.id))
                    .findFirst().ifPresent(estimation -> resultList.add(
                            new Res(
                                    estimationId,
                                    estimation.userId,
                                    estimation.userName,
                                    estimation.userEmail,
                                    estimation.userPhoneNumber,
                                    estimation.companyType,
                                    new EstimationAddress(
                                            estimation.chassisEstimationAddress.getZipCode(),
                                            getAddressStr(estimation.chassisEstimationAddress),
                                            estimation.chassisEstimationAddress.getRemainAddress(),
                                            convertToBoolean(
                                                    estimation.chassisEstimationAddress.getIsApartment()),
                                            convertToBoolean(
                                                    estimation.chassisEstimationAddress.getIsExpanded())
                                    ),
                                    estimation.totalPrice,
                                    estimation.createdAt,
                                    new AdditionalChassisPriceInfo(
                                            estimation.laborFee,
                                            estimation.ladderCarFee,
                                            estimation.demolitionFee,
                                            estimation.maintenanceFee,
                                            estimation.freightTransportFee,
                                            estimation.deliveryFee
                                    ),
                                    chassisSizeListOfEstimation
                            )));
        }

        return resultList.stream()
                .sorted(Comparator.comparing(
                        GetChassisEstimationInformationWebDtoV1.Res::createdAt).reversed())
                .collect(Collectors.toList());
    }

    private static String getAddressStr(final ChassisEstimationAddress target) {

        String state = target.getState();
        String city = target.getCity();
        String town = target.getTown();

        return state + " " + city + " " + town;
    }
}
