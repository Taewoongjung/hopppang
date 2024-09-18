package kr.hoppang.util.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.domain.chassis.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChassisPriceCalculator {

    private final static int valueOf10X10 = 100; // 10 X 10 에 대한 값

    private final AdditionalChassisPriceCriteriaRepository additionalChassisPriceCriteriaRepository;

    public int calculateMaterialPrice(
            final int priceFromDBOfWidthAndHeight,
            final int estimateWidth, // 실제 인입 된 너비에 대한 근사치
            final int estimateHeight, // 실제 인입 된 높이에 대한 근사치
            final int width, // 실제 인입 된 너비
            final int height // 실제 인입 된 높이
    ) {

        // 근사치에 대한 너비 (estimateWidth) 높이 (estimateHeight)에 대한 10X10을 구한다.
        BigDecimal approxDimension = BigDecimal.valueOf((long) estimateWidth * estimateHeight / valueOf10X10);
        BigDecimal approxPriceOf10X10 = BigDecimal.valueOf(priceFromDBOfWidthAndHeight).divide(approxDimension, MathContext.DECIMAL32);

        // 실제 인입에 대한 너비 (width) 높이 (height)에 대한 넓이를 구한다.
        BigDecimal dimension = BigDecimal.valueOf((long) width * height).divide(BigDecimal.valueOf(valueOf10X10), MathContext.DECIMAL32);

        return dimension.multiply(approxPriceOf10X10, MathContext.UNLIMITED).intValue();
    }

    // @TODO 층수에 따른 사다리차 비용 계산
    public int calculateLadderFee(final int floorCustomerLiving) {

        //  1층은 도수운반비 추가
        if (floorCustomerLiving == 1) {
            AdditionalChassisPriceCriteria freightTransportFee =
                    additionalChassisPriceCriteriaRepository.findByType(
                            AdditionalChassisPriceCriteriaType.FreightTransportFee);

            return freightTransportFee.getPrice();
        }

        // 2층 부터 사다리차 비용 추가
        if (floorCustomerLiving >= 2 && floorCustomerLiving <= 6) {
            AdditionalChassisPriceCriteria ladderCar2To6Fee =
                    additionalChassisPriceCriteriaRepository.findByType(
                            AdditionalChassisPriceCriteriaType.LadderCar2To6);

            return ladderCar2To6Fee.getPrice();

        } else if (floorCustomerLiving == 7 || 8 == floorCustomerLiving) {
            AdditionalChassisPriceCriteria ladderCar7To8Fee =
                    additionalChassisPriceCriteriaRepository.findByType(
                            AdditionalChassisPriceCriteriaType.LadderCar7To8);

            return ladderCar7To8Fee.getPrice();

        } else if (floorCustomerLiving == 9 || 10 == floorCustomerLiving) {
            AdditionalChassisPriceCriteria ladderCar9To10Fee =
                    additionalChassisPriceCriteriaRepository.findByType(
                            AdditionalChassisPriceCriteriaType.LadderCar9To10);

            return ladderCar9To10Fee.getPrice();

        } else {
            // 11층 이상일 때 계산
            AdditionalChassisPriceCriteria ladderCar11To22PerFloorFee =
                    additionalChassisPriceCriteriaRepository.findByType(
                            AdditionalChassisPriceCriteriaType.LadderCar11To22PerF);

            return ladderCar11To22PerFloorFee
                    .calculateLadderCarWhenOverFloor11(floorCustomerLiving);
        }
    }

    // @TODO 인건비 계산
    public int calculateLaborFee(
            final int widthForSingleWindow,
            final int heightForSingleWindow,
            final int widthForDoubleWindow,
            final int heightForDoubleWindow
    ) {
        AdditionalChassisPriceCriteria minimumLaborFee =
                additionalChassisPriceCriteriaRepository.findByType(
                        AdditionalChassisPriceCriteriaType.MinimumLaborFee);

        return minimumLaborFee.calculateLaborFee(
                widthForSingleWindow,
                heightForSingleWindow,
                widthForDoubleWindow,
                heightForDoubleWindow);
    }

    // @TODO 철거비 계산
    public int calculateDemolitionFee() {
        AdditionalChassisPriceCriteria demolitionFee =
                additionalChassisPriceCriteriaRepository.findByType(
                        AdditionalChassisPriceCriteriaType.DemolitionFee);

        return demolitionFee.getPrice();
    }

    // @TODO 보양비 계산
    public int calculateMaintenanceFee() {
        AdditionalChassisPriceCriteria maintenanceFee =
                additionalChassisPriceCriteriaRepository.findByType(
                        AdditionalChassisPriceCriteriaType.MaintenanceFee);

        return maintenanceFee.getPrice();
    }

    // @TODO 배송비 계산
    public int calculateDeliveryFee() {
        AdditionalChassisPriceCriteria deliveryFee =
                additionalChassisPriceCriteriaRepository.findByType(
                        AdditionalChassisPriceCriteriaType.DeliveryFee);

        return deliveryFee.getPrice();
    }
}
