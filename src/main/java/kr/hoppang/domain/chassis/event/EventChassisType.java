package kr.hoppang.domain.chassis.event;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum EventChassisType {
    BalconySingle,
    InteriorSingle,
    LivingRoomSliding,
    BalconyDouble,
    InteriorDouble,
    TotalPriceOver_800,
    TotalPriceOver_1000,
    TotalPriceOver_1300
    ;

    public static EventChassisType from(final String target) {
        return Arrays.stream(values())
                .filter(f->target.equals(f.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("EventChassisType 에 없는 필드 입니다."));
    }
}
