package kr.hoppang.adapter.outbound.jpa.entity.chassis.price.pricecriteria;

public enum AdditionalChassisPriceCriteriaType {
    DemolitionFee, // 철거비
    Demolition1To4Fee, // 1 ~ 4 틀 철거비
    DemolitionOver5Fee, // 5개 이상 틀 철거비
    MinimumLaborFee, // 최소 인건비
    MaintenanceFee, // 보양비
    LadderCar2To6, // 사다리차 2 ~ 6 층
    LadderCar7To8, // 사다리차 7 ~ 8 층
    LadderCar9To10, // 사다리차 9 ~ 10 층
    LadderCar11To22PerF, // 사다리차 11 ~ 22 층은 층 당 계산
    LadderCarOver23, // 사다리차 23 층 이상은 사다리차 불가능 할 수 있어서 22층 까지 계산 한 결과 리턴
    FreightTransportFee, // 도수 운반비
    DeliveryFee, // 배송비
    IncrementRate // 원가 대비 소비자요금으로 변환 비율
}
