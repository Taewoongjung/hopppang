package kr.hoppang.adapter.inbound.chassis.webdto;

import java.time.LocalDateTime;

public class GetChassisPriceAdditionalCriteriaWebDtoV1 {

    public record Req(String type,
                      int price,
                      LocalDateTime lastModified) {

    }
}
