package kr.hoppang.application.command.chassis.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AVAILABLE_MANUFACTURE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.adapter.outbound.jpa.entity.chassis.price.pricecriteria.AdditionalChassisPriceCriteriaType.*;
import static kr.hoppang.util.calculator.ChassisPriceCalculator.calculateSurtax;
import static kr.hoppang.util.common.BoolType.convertBooleanToType;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.event.ChassisDiscountEvent;
import kr.hoppang.domain.chassis.event.DiscountType;
import kr.hoppang.domain.chassis.event.EventChassisType;
import kr.hoppang.domain.chassis.event.repository.ChassisDiscountEventRepository;
import kr.hoppang.domain.chassis.price.ChassisPriceInfo;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.price.repository.ChassisPriceInfoRepository;
import kr.hoppang.domain.chassis.price.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ChassisPriceCalculator chassisPriceCalculator;
    private final ChassisPriceInfoRepository chassisPriceInfoRepository;
    private final ChassisDiscountEventRepository chassisDiscountEventRepository;
    private final AddChassisEstimationInfoCommandHandler addChassisEstimationInfoCommandHandler;
    private final AdditionalChassisPriceCriteriaRepository additionalChassisPriceCriteriaRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public CalculateChassisPriceCommandHandlerCommandResult handle(
            final CalculateChassisPriceCommand event) {

        log.info("[핸들러 - 샤시 내기] CalculateChassisPriceCommand = {}", event);

        AdditionalChassisPriceCriteria additionalChassisPriceCriteria = additionalChassisPriceCriteriaRepository.findByType(
                IncrementRate);

        List<CalculateChassisPrice> chassisReqList = event.calculateChassisPriceList();

        if (chassisReqList.isEmpty()) {
            return null;
        }

        List<Integer> calculatedPriceResultList = new ArrayList<>();
        List<ChassisPriceResult> chassisPriceResultList = new ArrayList<>();

        // 인건비를 위한 이중창 가로 세로 길이 저장 변수
        AtomicInteger widthForSingleWindow = new AtomicInteger(0);
        AtomicInteger heightForSingleWindow = new AtomicInteger(0);

        // 인건비를 위한 이중창 가로 세로 길이 저장 변수
        AtomicInteger widthForDoubleWindow = new AtomicInteger(0);
        AtomicInteger heightForDoubleWindow = new AtomicInteger(0);

        // 창호 자재비를 계산한다.
        chassisReqList.forEach(chassis -> {
            check(chassis.width() > 5000 || chassis.width() < 300, NOT_AVAILABLE_MANUFACTURE);
            check(chassis.height() > 2600 || chassis.height() < 300, NOT_AVAILABLE_MANUFACTURE);

            int chassisFinalPrice = 0;

            int chassisPrice = 0;

            int approxWidth = ApproximateCalculator.getApproximateWidth(chassis.width());
            int approxHeight = ApproximateCalculator.getApproximateHeight(chassis.height());

            if (!"고정값창".equals(chassis.chassisType().getType())) {
                if ("단창".equals(chassis.chassisType().getType())) {
                    widthForSingleWindow.addAndGet(approxWidth);
                    heightForSingleWindow.addAndGet(approxHeight);
                } else {
                    widthForDoubleWindow.addAndGet(approxWidth);
                    heightForDoubleWindow.addAndGet(approxHeight);
                }

                ChassisPriceInfo chassisPriceInfo =
                        chassisPriceInfoRepository.findByTypeAndCompanyTypeAndWidthAndHeight(
                                chassis.chassisType(), chassis.companyType(), approxWidth,
                                approxHeight);

                chassisPrice = chassisPriceCalculator.calculateMaterialPrice(
                        chassisPriceInfo.getPrice(),
                        approxWidth, approxHeight,
                        chassis.width(), chassis.height()
                );

                // 마진율 대비 계산
                chassisFinalPrice = calculateChassisPriceWithIncrementRate(
                        additionalChassisPriceCriteria.getPrice(),
                        chassisPrice
                );
            } else {
                ChassisPriceInfo chassisPriceInfo =
                        chassisPriceInfoRepository.findByTypeAndCompanyTypeAndWidthAndHeight(
                                chassis.chassisType(), chassis.companyType(), approxWidth,
                                approxHeight);
                // "고정값창"은 가격 데이터베이스에 있는 값 그대로.
                chassisFinalPrice = chassisPriceInfo.getPrice();
            }

            chassisPriceResultList.add(
                    new ChassisPriceResult(
                            chassis.chassisType(),
                            chassis.width(),
                            chassis.height(),
                            chassisFinalPrice
                    )
            );
        });

        // 층수에 따른 사다리차 비용을 계산한다
        int ladderCarFee = chassisPriceCalculator.calculateLadderFee(event.floorCustomerLiving());
        calculatedPriceResultList.add(ladderCarFee);

        // 철거시, 철거비를 계산한다.
        int demolitionFee = 0;
        if (event.isScheduledForDemolition()) {
            demolitionFee = chassisPriceCalculator.calculateDemolitionFee(
                    chassisReqList.size() >= 5 ? Demolition1To4Fee : DemolitionOver5Fee
            );
            calculatedPriceResultList.add(demolitionFee);
        }

        // 살고있을 시, 보양비를 계산한다.
        int maintenanceFee = 0;
        if (event.isResident()) {
            maintenanceFee = chassisPriceCalculator.calculateMaintenanceFee();
            calculatedPriceResultList.add(maintenanceFee);
        }

        int deliveryFee = chassisPriceCalculator.calculateDeliveryFee();
        calculatedPriceResultList.add(deliveryFee);

        User user = userRepository.findById(event.userId(), false);

        // 슬랙 알림 발송
        eventPublisher.publishEvent(
                NewEstimation.of(
                        user.getName(),
                        event.zipCode(),
                        event.state(),
                        event.city(),
                        event.town(),
                        event.remainAddress(),
                        chassisReqList.getFirst().companyType(),
                        event.calculateChassisPriceList()
                ));

        // 인건비를 계산한다.
        int laborFee = chassisPriceCalculator.calculateLaborFee(
                widthForSingleWindow.get(),
                heightForSingleWindow.get(),
                widthForDoubleWindow.get(),
                heightForDoubleWindow.get()
        );

        // 인건비를 견적 받은 샤시의 개수만큼 나눠서 각각 금액에 더한다.
        if (laborFee != 0 && !chassisPriceResultList.isEmpty()) {
            int dividedLaborFeeByCountOfChassis = laborFee / chassisPriceResultList.size();

            chassisPriceResultList
                    .forEach(e -> {
                        if (!"고정값창".equals(e.getChassisType().getType())) {
                            e.addLaborFeeToChassisPrice(dividedLaborFeeByCountOfChassis);
                        }
                    });
        }

        // 각 샤시 "퍼센트 할인" 정보 조회
        List<ChassisDiscountEvent> chassisRateDiscountEvent = getChassisDiscountEventInformation(
                chassisReqList, DiscountType.PERCENTAGE);

        // "퍼센트" 할인 이벤트 적용 혹은 샤시 가격 적용
        if (chassisRateDiscountEvent != null && !chassisRateDiscountEvent.isEmpty()) {
                chassisPriceResultList.forEach(
                        chassis -> {
                            ChassisDiscountEvent chassisDiscountEvent = getDiscountEventByChassisType(
                                    chassisRateDiscountEvent,
                                    EventChassisType.from(chassis.getChassisType().name())
                            );

                            if (chassisDiscountEvent != null) {
                                Integer discountedPrice = calculateChassisPriceWithDiscountRate(
                                        chassisDiscountEvent.getDiscountRate(), chassis.getPrice()
                                );

                                chassis.setDiscount(
                                        chassisDiscountEvent.getId(),
                                        chassisDiscountEvent.getDiscountRate(),
                                        discountedPrice
                                );

                                calculatedPriceResultList.add(discountedPrice);
                            } else {
                                calculatedPriceResultList.add(chassis.getPrice());
                            }
                        }
                );
        } else {
            int allChassisPrice = chassisPriceResultList.stream().mapToInt(ChassisPriceResult::getPrice).sum();
            calculatedPriceResultList.add(allChassisPrice);
        }

        // 각 비용 최종 가격
        int ladderFee = event.floorCustomerLiving() <= 1 ? 0
                : ladderCarFee; // ladderFee : 1층 이하면 사다리차 비용이 도수 운반비이다.
        int freightTransportFee = event.floorCustomerLiving() <= 1 ? ladderCarFee
                : 0; // freightTransportFee : 1층 이하면 사다리차 비용이 도수 운반비로 치환 된다
        int floor = event.floorCustomerLiving();

        /* 총 가격 계산 start */

        // 각 샤시 "고정 금액 할인" 정보 조회
        int totalPrice = calculatedPriceResultList.stream().mapToInt(Integer::intValue).sum();

        List<ChassisDiscountEvent> chassisFixedAmountDiscountEvents = getChassisDiscountEventInformation(
                chassisReqList, DiscountType.FIXED_AMOUNT);

        Long chassisDiscountEventId = null;
        Integer discountedTotalPrice = null;
        ChassisDiscountEvent appliedChassisFixedAmountDiscountEvent = null;
        if (chassisFixedAmountDiscountEvents != null &&
                !chassisFixedAmountDiscountEvents.isEmpty()
        ) {
            FixedAmountDiscount fixedAmountDiscount = calculateFixedAmountDiscount(
                    totalPrice,
                    chassisFixedAmountDiscountEvents
            );

            if (fixedAmountDiscount != null) {
                appliedChassisFixedAmountDiscountEvent = fixedAmountDiscount.appliedChassisDiscountEvent;
                discountedTotalPrice = fixedAmountDiscount.discountedTotalPrice;
                chassisDiscountEventId = fixedAmountDiscount.chassisDiscountEventId;
            }
        }
        /* 총 가격 계산 end */

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
                chassisReqList.getFirst().companyType().name(),
                deliveryFee,
                demolitionFee,
                maintenanceFee,
                laborFee,
                ladderFee,
                freightTransportFee,
                floor,
                additionalChassisPriceCriteria.getPrice(),
                totalPrice,
                chassisDiscountEventId,
                discountedTotalPrice,
                event.floorCustomerLiving(),
                user
        );

        log.info("[핸들러 - 샤시 내기] 성공");

        return new CalculateChassisPriceCommandHandlerCommandResult(
                registeredEstimationId,
                chassisReqList.getFirst().companyType().name(),
                chassisPriceResultList,
                deliveryFee,
                demolitionFee,
                maintenanceFee,
                ladderFee,
                freightTransportFee,
                floor,
                totalPrice,
                appliedChassisFixedAmountDiscountEvent != null ?
                        appliedChassisFixedAmountDiscountEvent.getDiscountRate() : null,
                discountedTotalPrice
        );
    }

    private List<ChassisDiscountEvent> getChassisDiscountEventInformation(
            final List<CalculateChassisPrice> chassisReqList,
            final DiscountType discountType
    ) {

        CompanyType companyType = chassisReqList.getFirst().companyType();

        List<ChassisType> chassisTypes = chassisReqList.stream()
                .map(CalculateChassisPrice::chassisType)
                .toList();

        return chassisDiscountEventRepository.findAllChassisDiscountEventByCompanyAndChassisType(
                companyType, chassisTypes, discountType
        );
    }

    private int calculateChassisPriceWithIncrementRate(
            final int incrementRateForConvertingToCustomerPrice,
            final int calculatedPriceResult
    ) {

        BigDecimal rate = BigDecimal.valueOf(incrementRateForConvertingToCustomerPrice)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal convertedTotalPrice = BigDecimal.valueOf(calculatedPriceResult)
                .multiply(rate.add(BigDecimal.ONE));

        return convertedTotalPrice.intValue();
    }

    private ChassisDiscountEvent getDiscountEventByChassisType(
            List<ChassisDiscountEvent> discountEventList,
            final EventChassisType eventChassisType
    ) {
        return discountEventList.stream()
                .filter(f -> eventChassisType.equals(f.getChassisType()))
                .findFirst()
                .orElse(null);
    }

    private FixedAmountDiscount calculateFixedAmountDiscount(
            final int totalPrice,
            final List<ChassisDiscountEvent> chassisFixedAmountDiscountEvents
    ) {
        Long chassisDiscountEventId = null;
        Integer discountedTotalPrice = null;

        int totalPriceWithSurtax = totalPrice + calculateSurtax(totalPrice);

        if (totalPrice >= 8000000 && totalPrice < 10000000) {

            ChassisDiscountEvent chassisDiscountEvent = chassisFixedAmountDiscountEvents.stream()
                    .filter(f -> EventChassisType.TotalPriceOver_800.equals(f.getChassisType()))
                    .findFirst()
                    .orElse(null);

            if (chassisDiscountEvent == null) {
                return null;
            }

            chassisDiscountEventId = chassisDiscountEvent.getId();
            discountedTotalPrice = totalPriceWithSurtax - chassisDiscountEvent.getDiscountRate();

            return new FixedAmountDiscount(
                    chassisDiscountEvent,
                    chassisDiscountEventId,
                    discountedTotalPrice
            );
        }

        if (totalPrice >= 10000000 && totalPrice < 13000000) {

            ChassisDiscountEvent chassisDiscountEvent = chassisFixedAmountDiscountEvents.stream()
                    .filter(f -> EventChassisType.TotalPriceOver_1000.equals(f.getChassisType()))
                    .findFirst()
                    .orElse(null);

            if (chassisDiscountEvent == null) {
                return null;
            }

            chassisDiscountEventId = chassisDiscountEvent.getId();
            discountedTotalPrice = totalPriceWithSurtax - chassisDiscountEvent.getDiscountRate();

            return new FixedAmountDiscount(
                    chassisDiscountEvent,
                    chassisDiscountEventId,
                    discountedTotalPrice
            );
        }

        if (totalPrice >= 13000000) {

            ChassisDiscountEvent chassisDiscountEvent = chassisFixedAmountDiscountEvents.stream()
                    .filter(f -> EventChassisType.TotalPriceOver_1300.equals(f.getChassisType()))
                    .findFirst()
                    .orElse(null);

            if (chassisDiscountEvent == null) {
                return null;
            }

            chassisDiscountEventId = chassisDiscountEvent.getId();
            discountedTotalPrice = totalPriceWithSurtax - chassisDiscountEvent.getDiscountRate();

            return new FixedAmountDiscount(
                    chassisDiscountEvent,
                    chassisDiscountEventId,
                    discountedTotalPrice
            );
        }

        return new FixedAmountDiscount(null, null, totalPrice);
    }

    private record FixedAmountDiscount(
            ChassisDiscountEvent appliedChassisDiscountEvent,
            Long chassisDiscountEventId,
            Integer discountedTotalPrice
    ) { }


    private int calculateChassisPriceWithDiscountRate(
            final int discountRate,
            final int calculatedPriceResult
    ) {

        BigDecimal dcRate = BigDecimal.valueOf(discountRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalPrice = BigDecimal.valueOf(calculatedPriceResult);

        BigDecimal discountedPrice = totalPrice.multiply(dcRate);

        return totalPrice.subtract(discountedPrice).intValue();
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
            final int appliedIncrementRate,
            final int totalPrice,
            final Long discountEventId,
            final Integer discountedTotalPrice,
            final int floorCustomerLiving,
            final User user) {

        List<ChassisSize> chassisSizeList = new ArrayList<>();

        for (ChassisPriceResult chassisPriceResult : chassisPriceResultList) {
            chassisSizeList.add(
                    new ChassisSize(
                            chassisPriceResult.getChassisType().name(),
                            chassisPriceResult.getWidth(),
                            chassisPriceResult.getHeight(),
                            chassisPriceResult.getPrice(),
                            chassisPriceResult.getChassisDiscountEventId(),
                            chassisPriceResult.getDiscountedPrice()
                    )
            );
        }

        return addChassisEstimationInfoCommandHandler.handle(
                new AddChassisEstimationInfoCommand(
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
                                appliedIncrementRate,
                                totalPrice,
                                discountEventId,
                                discountedTotalPrice,
                                floorCustomerLiving,
                                chassisSizeList,
                                user
                        )
                )
        );
    }
}
