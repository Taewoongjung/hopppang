package kr.hoppang.application.command.chassis.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AVAILABLE_MANUFACTURE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.common.BoolType.convertBooleanToType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.alarm.dto.NewEstimation;
import kr.hoppang.application.command.chassis.commandresults.CalculateChassisPriceCommandHandlerCommandResult;
import kr.hoppang.application.command.chassis.commandresults.CalculateChassisPriceCommandHandlerCommandResult.ChassisPriceResult;
import kr.hoppang.application.command.chassis.commands.AddChassisEstimationInfoCommand;
import kr.hoppang.application.command.chassis.commands.AddChassisEstimationInfoCommand.ChassisEstimationCommand;
import kr.hoppang.application.command.chassis.commands.AddChassisEstimationInfoCommand.ChassisEstimationCommand.ChassisSize;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand;
import kr.hoppang.application.command.chassis.commands.CalculateChassisPriceCommand.CalculateChassisPrice;
import kr.hoppang.domain.chassis.price.ChassisPriceInfo;
import kr.hoppang.domain.chassis.price.repository.ChassisPriceInfoRepository;
import kr.hoppang.util.calculator.ApproximateCalculator;
import kr.hoppang.util.calculator.ChassisPriceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateChassisPriceCommandHandler implements
        ICommandHandler<CalculateChassisPriceCommand, CalculateChassisPriceCommandHandlerCommandResult> {

    private final ApplicationEventPublisher eventPublisher;
    private final ChassisPriceCalculator chassisPriceCalculator;
    private final ChassisPriceInfoRepository chassisPriceInfoRepository;
    private final AddChassisEstimationInfoCommandHandler addChassisEstimationInfoCommandHandler;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public CalculateChassisPriceCommandHandlerCommandResult handle(final CalculateChassisPriceCommand event) {

        log.info("[핸들러 - 샤시 내기 ] CalculateChassisPriceCommand = {}", event);

        List<CalculateChassisPrice> reqList = event.calculateChassisPriceList();

        if (reqList.size() == 0) {
            return null;
        }

        List<Integer> calculatedResultList = new ArrayList<>();
        List<ChassisPriceResult> chassisPriceResultList = new ArrayList<>();

        // 인건비를 위한 이중창 가로 세로 길이 저장 변수
        AtomicInteger widthForSingleWindow = new AtomicInteger(0);
        AtomicInteger heightForSingleWindow = new AtomicInteger(0);

        // 인건비를 위한 이중창 가로 세로 길이 저장 변수
        AtomicInteger widthForDoubleWindow = new AtomicInteger(0);
        AtomicInteger heightForDoubleWindow = new AtomicInteger(0);

        // 자재비를 계산한다.
        reqList.forEach(chassis -> {
            check(chassis.width() > 5000 || chassis.width() < 300, NOT_AVAILABLE_MANUFACTURE);
            check(chassis.height() > 2600 || chassis.height() < 300, NOT_AVAILABLE_MANUFACTURE);

            int chassisPrice = 0;

            int approxWidth = ApproximateCalculator.getApproximateWidth(chassis.width());
            int approxHeight = ApproximateCalculator.getApproximateHeight(chassis.height());

            if ("단창".equals(chassis.chassisType().getType())) {
                widthForSingleWindow.addAndGet(approxWidth);
                heightForSingleWindow.addAndGet(approxHeight);
            } else {
                widthForDoubleWindow.addAndGet(approxWidth);
                heightForDoubleWindow.addAndGet(approxHeight);
            }

            ChassisPriceInfo chassisPriceInfo =
                    chassisPriceInfoRepository.findByTypeAndCompanyTypeAndWidthAndHeight(
                            chassis.chassisType(), chassis.companyType(), approxWidth, approxHeight);

            chassisPrice = chassisPriceCalculator.calculateMaterialPrice(
                    chassisPriceInfo.getPrice(),
                    approxWidth, approxHeight,
                    chassis.width(), chassis.height()
            );

            calculatedResultList.add(chassisPrice);

            chassisPriceResultList.add(new ChassisPriceResult(
                    chassis.chassisType().name(), chassis.width(), chassis.height(), chassisPrice)
            );
        });

        // 층수에 따른 사다리차 비용을 계산한다
        int ladderCarFee = chassisPriceCalculator.calculateLadderFee(event.floorCustomerLiving());
        calculatedResultList.add(ladderCarFee);

        // 인건비를 계산한다.
        int laborFee = chassisPriceCalculator.calculateLaborFee(
                widthForSingleWindow.get(),
                heightForSingleWindow.get(),
                widthForDoubleWindow.get(),
                heightForDoubleWindow.get());
        calculatedResultList.add(laborFee);

        // 철거시, 철거비를 계산한다.
        int demolitionFee = 0;
        if (event.isScheduledForDemolition()) {
            demolitionFee = chassisPriceCalculator.calculateDemolitionFee();
            calculatedResultList.add(demolitionFee);
        }

        // 살고있을 시, 보양비를 계산한다.
        int maintenanceFee = 0;
        if (event.isResident()) {
            maintenanceFee = chassisPriceCalculator.calculateMaintenanceFee();
            calculatedResultList.add(maintenanceFee);
        }

        int deliveryFee = chassisPriceCalculator.calculateDeliveryFee();
        calculatedResultList.add(deliveryFee);

        // 슬랙 알림 발송
        eventPublisher.publishEvent(NewEstimation.of(
            null, null,
                reqList.get(0).companyType(),
                event.calculateChassisPriceList()
        ));

        // 인건비를 견적 받은 샤시의 개수만큼 나눠서 각각 금액에 더한다.
        if (laborFee != 0) {
            int dividedLaborFeeByCountOfChassis = laborFee / chassisPriceResultList.size();

            chassisPriceResultList.forEach(e -> e.addLaborFeeToChassisPrice(dividedLaborFeeByCountOfChassis));
        }

        // 각 비용 최종 가격
        int ladderFee = event.floorCustomerLiving() <= 1 ? 0
                : ladderCarFee; // ladderFee : 1층 이하면 사다리차 비용이 도수 운반비이다.
        int freightTransportFee = event.floorCustomerLiving() <= 1 ? ladderCarFee
                : 0; // freightTransportFee : 1층 이하면 사다리차 비용이 도수 운반비로 치환 된다
        int floor = event.floorCustomerLiving();
        int totalPrice = calculatedResultList.stream().mapToInt(Integer::intValue).sum();

        long registeredEstimationId = registerChassisEstimation(
                chassisPriceResultList,
                event.zipCode(),
                event.state(),
                event.city(),
                event.town(),
                event.bCode(),
                event.remainAddress(),
                event.buildingNumber(),
                event.isApartment(),
                event.isExpanded(),
                reqList.get(0).companyType().name(),
                deliveryFee,
                demolitionFee,
                maintenanceFee,
                laborFee,
                ladderFee,
                freightTransportFee,
                floor,
                totalPrice,
                event.floorCustomerLiving()
        );

        log.info("[핸들러 - 샤시 내기] 성공");

        return new CalculateChassisPriceCommandHandlerCommandResult(
                registeredEstimationId,
                reqList.get(0).companyType().name(),
                chassisPriceResultList,
                deliveryFee,
                demolitionFee,
                maintenanceFee,
                ladderFee,
                freightTransportFee,
                floor,
                totalPrice
        );
    }

    private long registerChassisEstimation(
            final List<ChassisPriceResult> chassisPriceResultList,
            final String zipCode,
            final String state,
            final String city,
            final String town,
            final String bCode,
            final String remainAddress,
            final String buildingNumber,
            final boolean isApartment,
            final boolean isExpanded,
            final String companyName,
            final int deliveryFee,
            final int demolitionFee,
            final int maintenanceFee,
            final int laborFee,
            final int ladderFee,
            final int freightTransportFee,
            final int floor,
            final int totalPrice,
            final int floorCustomerLiving) {

        List<ChassisSize> chassisSizeList = new ArrayList<>();

        for (ChassisPriceResult chassisPriceResult : chassisPriceResultList) {
            chassisSizeList.add(new ChassisSize(
                    chassisPriceResult.getChassisType(),
                    chassisPriceResult.getWidth(),
                    chassisPriceResult.getHeight(),
                    chassisPriceResult.getPrice()));
        }

        return addChassisEstimationInfoCommandHandler.handle(new AddChassisEstimationInfoCommand(
                new ChassisEstimationCommand(
                        zipCode,
                        state,
                        city,
                        town,
                        bCode,
                        remainAddress,
                        buildingNumber,
                        convertBooleanToType(isApartment),
                        convertBooleanToType(isExpanded),
                        companyName,
                        deliveryFee,
                        demolitionFee,
                        maintenanceFee,
                        laborFee,
                        ladderFee,
                        freightTransportFee,
                        floor,
                        totalPrice,
                        floorCustomerLiving,
                        chassisSizeList
                )
        ));
    }
}
