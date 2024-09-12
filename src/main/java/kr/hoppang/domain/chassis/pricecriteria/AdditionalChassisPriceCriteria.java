package kr.hoppang.domain.chassis.pricecriteria;

import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.pricecriteria.AdditionalChassisPriceCriteriaType;
import kr.hoppang.domain.chassis.ChassisType;
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
            final ChassisType chassisType,
            final int approxWidth,
            final int approxHeight
    ) {
        /*
        * 단창 평당 인건비 3600 원
        * 이중창 평당 인건비 4600 원
        * */

        // 창 종류에 따른 평당 인건비
        int laborFeeByAPyeong = "단창".equals(chassisType.getType()) ? 3600 : 4600;

        // 평수 계산
        int pyeong = (approxWidth/300) * (approxHeight/300);

        int laborFee = pyeong * laborFeeByAPyeong;

        // 최소 인건비에 비해 해당 평수의 인건비가 저렴할 경우, "최소 인건비" 와 "평수의 인건비"의 차를 반환한다.
        // 최소 인건비에 비해 해당 평수의 인건비가 비쌀 경우, "평수의 인건비"의 차를 반환한다.
        if (this.price > laborFee) {
            return this.price - laborFee;
        } else {
            return laborFee;
        }
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
