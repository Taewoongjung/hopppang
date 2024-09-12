package kr.hoppang.adapter.inbound.chassis.webdto;

import java.time.LocalDateTime;

public class GetChassisPriceAdditionalCriteriaWebDtoV1 {

    public record Res(String type,
                      int price,
                      LocalDateTime lastModified) {

    }
}
