package kr.hoppang.adapter.inbound.chassis.webdto;

import java.util.List;
import kr.hoppang.domain.chassis.ChassisPriceInfo;

public class GetChassisPriceInformationWebDtoV1 {

    public record Res(List<ChassisPriceInfo> chassisPriceInfoList) {

    }
}