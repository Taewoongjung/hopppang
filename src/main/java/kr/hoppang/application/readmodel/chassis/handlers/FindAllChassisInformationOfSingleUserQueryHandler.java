package kr.hoppang.application.readmodel.chassis.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindAllChassisInformationOfSingleUserQuery;
import kr.hoppang.application.readmodel.chassis.queryresults.FindAllChassisInformationOfSingleUserQueryResult;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
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

        Slice<ChassisEstimationInfo> content = chassisEstimationRepository.findChassisEstimationInfoByUserId(
                query.userSrl(),
                query.pageable(),
                query.lastEstimationId());

        return FindAllChassisInformationOfSingleUserQueryResult.builder()
                .chassisEstimationInfoList(content.getContent())
                .isEndOfList(content.isLast())
                .build();
    }
}
