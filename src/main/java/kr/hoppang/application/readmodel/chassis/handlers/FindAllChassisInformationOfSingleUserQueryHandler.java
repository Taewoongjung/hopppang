package kr.hoppang.application.readmodel.chassis.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto.FindChassisEstimationInfoByUserIdRepositoryDto;
import kr.hoppang.application.readmodel.chassis.queries.FindAllChassisInformationOfSingleUserQuery;
import kr.hoppang.application.readmodel.chassis.queryresults.FindAllChassisInformationOfSingleUserQueryResult;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindAllChassisInformationOfSingleUserQueryHandler implements
        IQueryHandler<FindAllChassisInformationOfSingleUserQuery, FindAllChassisInformationOfSingleUserQueryResult> {

    private final ChassisEstimationRepository chassisEstimationRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public FindAllChassisInformationOfSingleUserQueryResult handle(
            final FindAllChassisInformationOfSingleUserQuery query) {

        Slice<FindChassisEstimationInfoByUserIdRepositoryDto.Response> content = chassisEstimationRepository.findChassisEstimationInfoByUserId(
                query.userId(),
                query.pageable(),
                query.lastEstimationId());

        return FindAllChassisInformationOfSingleUserQueryResult.builder()
                .chassisEstimationInfoList(
                        content.getContent().stream()
                                .map(estimation ->
                                        ChassisEstimationInfo.builder()
                                                .id(estimation.id())
                                                .userId(estimation.userId())
                                                .companyType(estimation.companyType())
                                                .ladderCarFee(estimation.ladderCarFee())
                                                .demolitionFee(estimation.demolitionFee())
                                                .maintenanceFee(estimation.maintenanceFee())
                                                .freightTransportFee(
                                                        estimation.freightTransportFee())
                                                .deliveryFee(estimation.deliveryFee())
                                                .appliedIncrementRate(
                                                        estimation.appliedIncrementRate())
                                                .totalPrice(estimation.totalPrice())
                                                .customerLivingFloor(
                                                        estimation.customerLivingFloor())
                                                .createdAt(estimation.createdAt())
                                                .chassisEstimationAddress(
                                                        ChassisEstimationAddress.builder()
                                                                .zipCode(estimation.address().zipCode())
                                                                .state(estimation.address().state())
                                                                .city(estimation.address().city())
                                                                .town(estimation.address().town())
                                                                .remainAddress(
                                                                        estimation.address()
                                                                                .remainAddress()
                                                                )
                                                                .build()
                                                )
                                                .chassisEstimationSizeInfoList(
                                                        estimation.chassisEstimationSizeInfoList()
                                                                .stream()
                                                                .map(chassisEstimationSizeInfo ->
                                                                        ChassisEstimationSizeInfo.builder()
                                                                                .chassisType(
                                                                                        chassisEstimationSizeInfo.chassisType())
                                                                                .width(chassisEstimationSizeInfo.width())
                                                                                .height(chassisEstimationSizeInfo.height())
                                                                                .build()
                                                                ).toList()
                                                )
                                                .build()
                                ).toList()
                )
                .isEndOfList(!content.hasNext())
                .build();
    }
}
