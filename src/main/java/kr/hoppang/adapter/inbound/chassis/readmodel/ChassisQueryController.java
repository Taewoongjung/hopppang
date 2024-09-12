package kr.hoppang.adapter.inbound.chassis.readmodel;

import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceAdditionalCriteriaWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceInformationWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceInformationWebDtoV1.Res;
import kr.hoppang.application.readmodel.chassis.handlers.FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceInfoByCompanyTypeQuery;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chassis")
public class ChassisQueryController {

    private final FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler findChassisPriceInfoByCompanyTypeQueryHandler;

    @GetMapping(value = "/prices")
    public ResponseEntity<GetChassisPriceInformationWebDtoV1.Res> getChassisPriceInformation(
            @RequestParam(value = "companyType") final CompanyType companyType,
            @RequestParam(value = "chassisType") final ChassisType chassisType
            ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Res(
                        findChassisPriceInfoByCompanyTypeQueryHandler.handle(
                                new FindChassisPriceInfoByCompanyTypeQuery(chassisType, companyType)
                        )
                ));
    }

    @GetMapping(value = "/prices/additions/criteria")
    public ResponseEntity<GetChassisPriceAdditionalCriteriaWebDtoV1.Req> getChassisPriceAdditionalCriteria() {

        return ResponseEntity.status(HttpStatus.OK)
                .body();
    }
}
