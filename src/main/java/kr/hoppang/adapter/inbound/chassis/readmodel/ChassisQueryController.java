package kr.hoppang.adapter.inbound.chassis.readmodel;

import kr.hoppang.adapter.inbound.chassis.webdto.GetCalculatedChassisPriceWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceInformationWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceInformationWebDtoV1.Res;
import kr.hoppang.application.readmodel.chassis.handlers.CalculateChassisPriceQueryHandler;
import kr.hoppang.application.readmodel.chassis.handlers.FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceInfoByCompanyTypeQuery;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChassisQueryController {

    private final CalculateChassisPriceQueryHandler calculateChassisPriceQueryHandler;
    private final FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler findChassisPriceInfoByCompanyTypeQueryHandler;

    @GetMapping(value = "/api/chassis/prices")
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

    @PostMapping(value = "/api/chassis/calculations/prices")
    public ResponseEntity<Integer> getCalculatedChassisPrice(
            @RequestBody final GetCalculatedChassisPriceWebDtoV1.Req req
    ) {
        req.validate();

        return ResponseEntity.status(HttpStatus.OK)
                .body(calculateChassisPriceQueryHandler.handle(req.toQuery()));
    }
}
