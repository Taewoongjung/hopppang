package kr.hoppang.application.readmodel.chassis.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_REQUEST;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedChassisByIdQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedChassisByIdQuery.Req;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedChassisByIdQuery.Res;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;
import kr.hoppang.domain.chassis.event.repository.ChassisDiscountEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindEstimatedChassisByIdQueryHandler implements IQueryHandler<
        FindEstimatedChassisByIdQuery.Req, FindEstimatedChassisByIdQuery.Res> {

    private final ChassisEstimationRepository chassisEstimationRepository;
    private final ChassisDiscountEventRepository chassisDiscountEventRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Res handle(final Req query) {

        ChassisEstimationInfo chassisEstimationInfo = chassisEstimationRepository.findChassisEstimationInfoById(
                query.estimatedId(), true);

        check(!chassisEstimationInfo.getUserId().equals(query.queriedUserId()),
                INVALID_REQUEST);

        ChassisDiscountEvent chassisDiscountEvent = null;
        if (chassisEstimationInfo.getChassisDiscountEventId() != null) {
            chassisDiscountEvent = chassisDiscountEventRepository.findChassisDiscountEventById(
                    chassisEstimationInfo.getChassisDiscountEventId());
        }

        return Res.builder()
                .estimationInfo(chassisEstimationInfo)
                .chassisDiscountEvent(chassisDiscountEvent)
                .build();
    }
}
