package kr.hoppang.domain.chassis.pricecriteria;

import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import lombok.Getter;

@Getter
public class AdditionalChassisPriceCriteria {

    private final AdditionalChassisPriceCriteriaType type;
    private int price;
    private final LocalDateTime lastModified;
    private final LocalDateTime createdAt;

    private AdditionalChassisPriceCriteria(
            final AdditionalChassisPriceCriteriaType type,
            final int price,
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {
        this.type = type;
        this.price = price;
        this.lastModified = lastModified;
        this.createdAt = createdAt;
    }

    public static AdditionalChassisPriceCriteria of(
            final AdditionalChassisPriceCriteriaType type,
            final int price,
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {
        return new AdditionalChassisPriceCriteria(type, price, lastModified, createdAt);
    }

    public static AdditionalChassisPriceCriteria of(
            final AdditionalChassisPriceCriteriaType type,
            final int price
    ) {
        return new AdditionalChassisPriceCriteria(type, price, null, null);
    }

    public int calculateLaborFee(
            final int widthForSingleWindow,
            final int heightForSingleWindow,
            final int widthForDoubleWindow,
            final int heightForDoubleWindow
    ) {
        /*
        * 단창 평당 인건비 3600 원
        * 이중창 평당 인건비 4600 원
        * */
        int laborFeeOfSingleWindow = calculateSingleWindowLaborFee(widthForSingleWindow, heightForSingleWindow);
        int laborFeeOfDoubleWindow = calculateDoubleWindowLaborFee(widthForDoubleWindow, heightForDoubleWindow);

        int laborFee = laborFeeOfSingleWindow + laborFeeOfDoubleWindow;

        // 최소 인건비에 비해 해당 평수의 인건비가 저렴할 경우, "최소 인건비" 와 "평수의 인건비"의 차를 반환한다.
        // 최소 인건비에 비해 해당 평수의 인건비가 비쌀 경우, "평수의 인건비"의 차를 반환한다.
        if (this.price > laborFee) {
            return this.price - laborFee;
        } else {
            return laborFee;
        }
    }

    private int calculateSingleWindowLaborFee(final int width, final int height) {
        // 평당 인건비 (단창)
        int laborFeeByAPyeong = 3600;

        // 평수 계산
        int pyeong = (width/300) * (height/300);

        return pyeong * laborFeeByAPyeong;
    }

    private int calculateDoubleWindowLaborFee(final int width, final int height) {
        // 평당 인건비 (이중창)
        int laborFeeByAPyeong = 4600;

        // 평수 계산
        int pyeong = (width/300) * (height/300);

        return pyeong * laborFeeByAPyeong;
    }

    public int calculateLadderCarWhenOverFloor11(final int targetFloor) {

        int tmpTargetFloor = targetFloor;

        // reason : 23층 이상은 사다리차로 못가니깐, 사다리차가 갈 수 있는 최고층인 22층 비용을 부가한다.
        if (targetFloor >= 23) {
            tmpTargetFloor = 22;
        }

        return tmpTargetFloor * this.price;
    }

    public void revisePrice(final int price) {
        this.price = price;
    }

    public boolean comparePriceWithTarget(final int targetPrice) {
        return this.price == targetPrice;
    }
}
