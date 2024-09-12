package kr.hoppang.application.command.chassis.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AVAILABLE_MANUFACTURE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.ChassisPriceInfo;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.repository.ChassisPriceInfoRepository;
import kr.hoppang.util.calculator.ApproximateCalculator;
import kr.hoppang.util.calculator.ChassisPriceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateChassisPriceCommandHandler implements
        ICommandHandler<CalculateChassisPriceCommand, Integer> {

    private final ChassisPriceCalculator chassisPriceCalculator;
    private final ChassisPriceInfoRepository chassisPriceInfoRepository;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer handle(final CalculateChassisPriceCommand event) {

        List<CalculateChassisPrice> reqList = event.calculateChassisPriceList();

        if (reqList.size() == 0) {
            return 0;
        }

        List<Integer> calculatedResultList = new ArrayList<>();

        // 인건비를 위한 이중창 가로 세로 길이 저장 변수
        int widthForSingleWindow = 0;
        int heightForSingleWindow = 0;

        // 인건비를 위한 이중창 가로 세로 길이 저장 변수
        int widthForDoubleWindow = 0;
        int heightForDoubleWindow = 0;

        // 자재비를 계산한다.
        reqList.forEach(chassis -> {
            check(chassis.width() > 5000 || chassis.width() < 300, NOT_AVAILABLE_MANUFACTURE);
            check(chassis.height() > 2600 || chassis.height() < 300, NOT_AVAILABLE_MANUFACTURE);

            int chassisPrice = 0;

            int approxWidth = ApproximateCalculator.getApproximateWidth(chassis.width());
            int approxHeight = ApproximateCalculator.getApproximateHeight(chassis.height());

            assignDimensionsByWindowType(chassis.chassisType(),
                    approxWidth, approxHeight,
                    widthForSingleWindow,
                    heightForSingleWindow,
                    widthForDoubleWindow,
                    heightForDoubleWindow
            );

            ChassisPriceInfo chassisPriceInfo =
                    chassisPriceInfoRepository.findByTypeAndCompanyTypeAndWidthAndHeight(
                            chassis.chassisType(), chassis.companyType(), approxWidth, approxHeight);

            chassisPrice = chassisPriceCalculator.calculateMaterialPrice(
                    chassisPriceInfo.getPrice(),
                    approxWidth, approxHeight,
                    chassis.width(), chassis.height()
            );

            calculatedResultList.add(chassisPrice);
        });

        // 층수에 따른 사다리차 비용을 계산한다
        calculatedResultList.add(chassisPriceCalculator.calculateLadderFee(event.floorCustomerLiving()));

        // 인건비를 계산한다.
        calculatedResultList.add(chassisPriceCalculator.calculateLaborFee(
                widthForSingleWindow,
                heightForSingleWindow,
                widthForDoubleWindow,
                heightForDoubleWindow)
        );

        // 철거시, 철거비를 계산한다.
        if (event.isScheduledForDemolition()) {
            calculatedResultList.add(chassisPriceCalculator.calculateDemolitionFee());
        }

        // 살고있을 시, 보양비를 계산한다.
        if (event.isResident()) {
            calculatedResultList.add(chassisPriceCalculator.calculateFreightTransportFee());
        }

        return calculatedResultList.stream().mapToInt(Integer::intValue).sum();
    }

    private void assignDimensionsByWindowType(
            final ChassisType chassisType,
            final int approxWidth,
            final int approxHeight,
            int widthForSingleWindow,
            int heightForSingleWindow,
            int widthForDoubleWindow,
            int heightForDoubleWindow
    ) {
        if ("단창".equals(chassisType.getType())) {
            widthForSingleWindow = approxWidth;
            heightForSingleWindow = approxHeight;
        } else {
            widthForDoubleWindow = approxWidth;
            heightForDoubleWindow = approxHeight;
        }
    }
}
