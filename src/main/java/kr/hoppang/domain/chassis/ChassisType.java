package kr.hoppang.domain.chassis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChassisType {
    BalconySingle("단창"),     // 발코니단창
    InteriorSingle("단창"),    // 내창단창
    LivingRoomSliding("단창"), // 거실분합창
    BalconyDouble("이중창"),     // 발코니이중창
    InteriorDouble("이중창");     // 내창이중창

    private final String type;
}
