package kr.hoppang.adapter.inbound.chassis.readmodel;

import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceAdditionalCriteriaWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceInformationWebDtoV1;
import kr.hoppang.application.readmodel.chassis.handlers.FindChassisPriceAdditionalCriteriaQueryHandler;
import kr.hoppang.application.readmodel.chassis.handlers.FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceAdditionalCriteriaQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceInfoByCompanyTypeQuery;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
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

    private final FindChassisPriceAdditionalCriteriaQueryHandler findChassisPriceAdditionalCriteriaQueryHandler;
    private final FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler findChassisPriceInfoByCompanyTypeQueryHandler;

    @GetMapping(value = "/prices")
    public ResponseEntity<GetChassisPriceInformationWebDtoV1.Res> getChassisPriceInformation(
            @RequestParam(value = "companyType") final CompanyType companyType,
            @RequestParam(value = "chassisType") final ChassisType chassisType
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetChassisPriceInformationWebDtoV1.Res(
                        findChassisPriceInfoByCompanyTypeQueryHandler.handle(
                                new FindChassisPriceInfoByCompanyTypeQuery(chassisType, companyType)
                        )
                ));
    }

    @GetMapping(value = "/prices/additions/criteria")
    public ResponseEntity<List<GetChassisPriceAdditionalCriteriaWebDtoV1.Res>> getChassisPriceAdditionalCriteria() {

        List<AdditionalChassisPriceCriteria> responseList = findChassisPriceAdditionalCriteriaQueryHandler
                .handle(new FindChassisPriceAdditionalCriteriaQuery());

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseList.stream()
                        .map(e -> new GetChassisPriceAdditionalCriteriaWebDtoV1.Res(
                                e.getType().name(),
                                e.getPrice(),
                                e.getLastModified()
                        )).collect(Collectors.toList())
                );
    }
}
