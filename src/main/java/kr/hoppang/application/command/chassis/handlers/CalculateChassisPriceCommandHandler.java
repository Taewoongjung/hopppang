package kr.hoppang.application.command.chassis.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AVAILABLE_MANUFACTURE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.ChassisPriceInfo;
import kr.hoppang.domain.chassis.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.repository.ChassisPriceInfoRepository;
import kr.hoppang.domain.chassis.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
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

    private final ChassisPriceInfoRepository chassisPriceInfoRepository;
    private final AdditionalChassisPriceCriteriaRepository additionalChassisPriceCriteriaRepository;

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

        reqList.forEach(e -> {
            check(e.width() > 5000 || e.width() < 300, NOT_AVAILABLE_MANUFACTURE);
            check(e.height() > 2600 || e.height() < 300, NOT_AVAILABLE_MANUFACTURE);

            int finalResultChassisPrice = 0;

            int approxWidth = ApproximateCalculator.getApproximateWidth(e.width());
            int approxHeight = ApproximateCalculator.getApproximateHeight(e.height());

            ChassisPriceInfo chassisPriceInfo =
                    chassisPriceInfoRepository.findByTypeAndCompanyTypeAndWidthAndHeight(
                            e.chassisType(), e.companyType(), approxWidth, approxHeight);

            // 자재비를 계산한다.
            int chassisPriceResult = ChassisPriceCalculator.calculate(
                    chassisPriceInfo.getPrice(),
                    approxWidth, approxHeight,
                    e.width(), e.height()
            );

            finalResultChassisPrice += chassisPriceResult;

            // 층수에 따른 사다리차 비용을 계산한다
            //  1층은 도수운반비 추가
            if (e.floorCustomerLiving() == 1) {
                AdditionalChassisPriceCriteria freightTransportFee =
                        additionalChassisPriceCriteriaRepository.findByType(
                                AdditionalChassisPriceCriteriaType.FreightTransportFee);

                finalResultChassisPrice += freightTransportFee.getPrice();
            }

            // 2층 부터 사다리차 비용 추가
            if (e.floorCustomerLiving() >= 2 && e.floorCustomerLiving() <= 6) {
                AdditionalChassisPriceCriteria ladderCar2To6Fee =
                        additionalChassisPriceCriteriaRepository.findByType(
                                AdditionalChassisPriceCriteriaType.LadderCar2To6);

                finalResultChassisPrice += ladderCar2To6Fee.getPrice();

            } else if (e.floorCustomerLiving() == 7 || 8 == e.floorCustomerLiving()) {
                AdditionalChassisPriceCriteria ladderCar7To8Fee =
                        additionalChassisPriceCriteriaRepository.findByType(
                                AdditionalChassisPriceCriteriaType.LadderCar7To8);

                finalResultChassisPrice += ladderCar7To8Fee.getPrice();

            } else if (e.floorCustomerLiving() == 9 || 10 == e.floorCustomerLiving()) {
                AdditionalChassisPriceCriteria ladderCar9To10Fee =
                        additionalChassisPriceCriteriaRepository.findByType(
                                AdditionalChassisPriceCriteriaType.LadderCar9To10);

                finalResultChassisPrice += ladderCar9To10Fee.getPrice();

            } else if (e.floorCustomerLiving() >= 11) {
                AdditionalChassisPriceCriteria ladderCar11To22PerFloorFee =
                        additionalChassisPriceCriteriaRepository.findByType(
                                AdditionalChassisPriceCriteriaType.LadderCar11To22PerF);

                finalResultChassisPrice +=
                        ladderCar11To22PerFloorFee.calculateLadderCarWhenOverFloor11(
                                e.floorCustomerLiving());
            }


            // 인건비를 계산한다.
            AdditionalChassisPriceCriteria minimumLaborFee =
                    additionalChassisPriceCriteriaRepository.findByType(
                            AdditionalChassisPriceCriteriaType.MinimumLaborFee);

            int laborFee = minimumLaborFee.calculateLaborFee(e.chassisType(), approxWidth,
                    approxHeight);

            finalResultChassisPrice += laborFee;

            // 철거시, 철거비를 계산한다.
            if (e.isScheduledForDemolition()) {
                AdditionalChassisPriceCriteria demolitionFee =
                        additionalChassisPriceCriteriaRepository.findByType(
                                AdditionalChassisPriceCriteriaType.DemolitionFee);

                finalResultChassisPrice += demolitionFee.getPrice();
            }

            // 살고있을 시, 보양비를 계산한다.
            if (e.isResident()) {
                AdditionalChassisPriceCriteria maintenanceFee =
                        additionalChassisPriceCriteriaRepository.findByType(
                                AdditionalChassisPriceCriteriaType.MaintenanceFee);

                finalResultChassisPrice += maintenanceFee.getPrice();
            }

            calculatedResultList.add(finalResultChassisPrice);
        });

        return calculatedResultList.stream().mapToInt(Integer::intValue).sum();
    }
}
