package kr.hoppang.domain.chassis;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChassisType {
    BalconySingle("단창", "발코니단창"),
    InteriorSingle("단창", "내창단창"),
    LivingRoomSliding("단창", "거실분합창"),
    BalconyDouble("이중창", "발코니이중창"),
    InteriorDouble("이중창", "내창이중창"),
    Fixed("단창", "픽스창"),
    Turning("고정값창", "터닝도어");

    private final String type;
    private final String chassisName;

    public static ChassisType from(final String target) {
        return Arrays.stream(values())
                .filter(f->target.equals(f.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("ChassisType 에 없는 필드 입니다."));
    }
}
