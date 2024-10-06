package kr.hoppang.application.readmodel.chassis.handlers;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisEstimationInformationQuery;
import kr.hoppang.application.readmodel.chassis.queryresults.FindChassisEstimationInformationQueryHandlerResult;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationAddress;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import kr.hoppang.domain.chassis.estimation.repository.dto.FindChassisEstimationInfosResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindChassisEstimationInformationQueryHandler
        implements IQueryHandler<
            FindChassisEstimationInformationQuery,
            List<FindChassisEstimationInformationQueryHandlerResult>
        > {

    private final ChassisEstimationRepository chassisEstimationRepository;

    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FindChassisEstimationInformationQueryHandlerResult> handle(
            final FindChassisEstimationInformationQuery query) {

        List<FindChassisEstimationInfosResult> foundList =
                chassisEstimationRepository.findChassisEstimationInfosBy(
                        query.estimationId(),
                        query.companyType(),
                        query.chassisType(),
                        query.startTime(),
                        query.endTime(),
                        query.limit(), query.offset()
                );

        // 결과 객체 생성 후 리턴
        List<FindChassisEstimationInformationQueryHandlerResult> resultList = new ArrayList<>();

        foundList.forEach(e ->
                resultList.add(new FindChassisEstimationInformationQueryHandlerResult(
                        e.getId(),
                        e.getChassisType(),
                        e.getWidth(),
                        e.getHeight(),
                        e.getPrice(),
                        e.getUserId(),
                        ChassisEstimationAddress.of(
                                e.getZipCode(),
                                e.getState(),
                                e.getCity(),
                                e.getTown(),
                                null,
                                e.getRemainAddress(),
                                null,
                                e.getIsApartment(),
                                e.getIsExpanded()
                        ),
                        e.getCompanyType(),
                        e.getLaborFee(),
                        e.getLadderCarFee(),
                        e.getDemolitionFee(),
                        e.getMaintenanceFee(),
                        e.getFreightTransportFee(),
                        e.getDeliveryFee(),
                        e.getTotalPrice(),
                        e.getCreatedAt(),
                        e.getLastModified()
                ))
        );

        return resultList;
    }
}
