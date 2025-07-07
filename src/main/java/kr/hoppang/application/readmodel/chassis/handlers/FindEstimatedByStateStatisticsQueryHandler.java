package kr.hoppang.application.readmodel.chassis.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedByStateStatisticsQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedByStateStatisticsQuery.Res;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindEstimatedByStateStatisticsQueryHandler implements IQueryHandler<EmptyQuery, FindEstimatedByStateStatisticsQuery.Res> {

    private final ChassisEstimationRepository chassisEstimationRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Res handle(EmptyQuery query) {

        List<ChassisEstimationInfo> chassisEstimationInfoList = chassisEstimationRepository.findAll();

        Map<String, Integer> countOfEstimatedByState = initCountOfEstimatedByState();

        chassisEstimationInfoList.forEach(
                chassisEstimationInfo -> {
                    String targetState = getStateForMap(
                            chassisEstimationInfo.getChassisEstimationAddress().getState()
                    );

                    countOfEstimatedByState.put(
                            targetState,
                            countOfEstimatedByState.get(targetState) + 1
                    );
                }
        );

        return Res.builder()
                .countOfEstimatedByState(countOfEstimatedByState)
                .totalEstimationCount(chassisEstimationInfoList.size())
                .build();
    }

    private String getStateForMap(final String targetState) {
        if (targetState == null) {
            return "알수없음";
        }

        if ("서울".equals(targetState) || "경기".equals(targetState)) {
            return "서울/경기";
        }

        return targetState;
    }

    private Map<String, Integer> initCountOfEstimatedByState() {
        Map<String, Integer> countOfEstimatedByState = new HashMap<>();

        countOfEstimatedByState.put("서울/경기", 0);
        countOfEstimatedByState.put("부산", 0);
        countOfEstimatedByState.put("대구", 0);
        countOfEstimatedByState.put("인천", 0);
        countOfEstimatedByState.put("광주", 0);
        countOfEstimatedByState.put("대전", 0);
        countOfEstimatedByState.put("울산", 0);
        countOfEstimatedByState.put("세종특별자치시", 0);
        countOfEstimatedByState.put("충북", 0);
        countOfEstimatedByState.put("충남", 0);
        countOfEstimatedByState.put("전남", 0);
        countOfEstimatedByState.put("경북", 0);
        countOfEstimatedByState.put("경남", 0);
        countOfEstimatedByState.put("강원특별자치도", 0);
        countOfEstimatedByState.put("전북특별자치도", 0);
        countOfEstimatedByState.put("제주특별자치도", 0);

        return countOfEstimatedByState;
    }
}
