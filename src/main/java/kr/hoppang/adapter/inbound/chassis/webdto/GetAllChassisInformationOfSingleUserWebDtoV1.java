package kr.hoppang.adapter.inbound.chassis.webdto;

import static kr.hoppang.util.calculator.ChassisPriceCalculator.calculateSurtax;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.inbound.chassis.webdto.GetAllChassisInformationOfSingleUserWebDtoV1.Response.Estimation.Chassis;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import lombok.AccessLevel;
import lombok.Builder;

public record GetAllChassisInformationOfSingleUserWebDtoV1() {

    @Builder(access = AccessLevel.PRIVATE)
    public record Response(
            List<Estimation> estimationList,
            boolean isLastPage
    ) {

        @Builder(access = AccessLevel.PRIVATE)
        record Estimation(
            @JsonProperty(value = "estimationId")
            long id,

            @JsonProperty(value = "companyType")
            String companyType,

            @JsonProperty(value = "fullAddress")
            String fullAddress,

            @JsonProperty(value = "chassisList")
            List<Chassis> chassisList,

            @JsonProperty(value = "wholePrice")
            int wholePriceWithSurtax,

            @JsonProperty(value = "estimatedAt")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime estimatedAt
        ) {

            @Builder(access = AccessLevel.PRIVATE)
            record Chassis(
                    @JsonProperty(value = "type")
                    String type,

                    @JsonProperty(value = "width")
                    int width,

                    @JsonProperty(value = "height")
                    int height
            ) { }
        }

        public static Response of(
                final List<ChassisEstimationInfo> chassisEstimationInfoList,
                final boolean isEndOfList
        ) {

            return Response.builder()
                    .estimationList(
                            chassisEstimationInfoList.stream()
                                    .map(chassisEstimation ->
                                            Estimation.builder()
                                                    .id(chassisEstimation.getId())
                                                    .companyType(
                                                            chassisEstimation.getCompanyType()
                                                                    .getCompanyName()
                                                    )
                                                    .fullAddress(
                                                            getFullAddress(
                                                                    chassisEstimation.getChassisEstimationAddress()
                                                            )
                                                    )
                                                    .chassisList(
                                                            chassisEstimation.getChassisEstimationSizeInfoList()
                                                                    .stream()
                                                                    .map(chassisSizeInfo ->
                                                                            Chassis.builder()
                                                                                    .type(chassisSizeInfo.getChassisType()
                                                                                            .getChassisName())
                                                                                    .height(chassisSizeInfo.getHeight())
                                                                                    .width(chassisSizeInfo.getWidth())
                                                                                    .build()
                                                                    )
                                                                    .toList()
                                                    )
                                                    .wholePriceWithSurtax(
                                                            calculateSurtax(
                                                                    chassisEstimation.getTotalPrice()
                                                            )
                                                    )
                                                    .estimatedAt(chassisEstimation.getCreatedAt())
                                                    .build()
                                    )
                                    .toList()
                    )
                    .isLastPage(isEndOfList)
                    .build();
        }
    }

    private static String getFullAddress(final ChassisEstimationAddress chassisEstimationAddress) {
        return "[" + chassisEstimationAddress.getZipCode() + "] "
                + chassisEstimationAddress.getState() + " "
                + chassisEstimationAddress.getCity() + " "
                + chassisEstimationAddress.getTown() + " "
                + chassisEstimationAddress.getRemainAddress();
    }
}
