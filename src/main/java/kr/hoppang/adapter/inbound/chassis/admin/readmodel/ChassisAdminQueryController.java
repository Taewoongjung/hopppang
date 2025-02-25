package kr.hoppang.adapter.inbound.chassis.admin.readmodel;

import static kr.hoppang.adapter.common.util.VersatileUtil.convertStringToLocalDateTime;
import static kr.hoppang.application.readmodel.chassis.queryresults.FindChassisEstimationInformationQueryResult.toWebResponseObject;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.adapter.inbound.chassis.admin.webdto.GetAllEstimationsProvidedToCustomersWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.admin.webdto.GetEstimatedStatisticsWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisEstimationInformationWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceAdditionalCriteriaWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetChassisPriceInformationWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetCountOfChassisEstimationInformationWebDtoV1;
import kr.hoppang.application.readmodel.chassis.handlers.FindAllEstimationsProvidedToCustomersQueryHandler;
import kr.hoppang.application.readmodel.chassis.handlers.FindChassisEstimationInformationQueryHandler;
import kr.hoppang.application.readmodel.chassis.handlers.FindChassisPriceAdditionalCriteriaQueryHandler;
import kr.hoppang.application.readmodel.chassis.handlers.FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler;
import kr.hoppang.application.readmodel.chassis.handlers.FindEstimatedStatisticsQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.EmptyQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisEstimationInformationQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceAdditionalCriteriaQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceInfoByCompanyTypeQuery;
import kr.hoppang.application.readmodel.chassis.queries.FindEstimatedStatisticsQuery;
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
@RequestMapping(value = "/api/admin/chassis")
public class ChassisAdminQueryController {

    private final FindEstimatedStatisticsQueryHandler findEstimatedStatisticsQueryHandler;
    private final FindChassisEstimationInformationQueryHandler findChassisEstimationInformationQueryHandler;
    private final FindChassisPriceAdditionalCriteriaQueryHandler findChassisPriceAdditionalCriteriaQueryHandler;
    private final FindChassisPriceInfoByTypeAndCompanyTypeQueryHandler findChassisPriceInfoByCompanyTypeQueryHandler;
    private final FindAllEstimationsProvidedToCustomersQueryHandler findAllEstimationsProvidedToCustomersQueryHandler;


    @GetMapping(
            value = "/prices",
            produces = APPLICATION_JSON_VALUE
    )
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

    @GetMapping(
            value = "/prices/additions/criteria",
            produces = APPLICATION_JSON_VALUE
    )
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

    @GetMapping(value = "/estimations")
    public ResponseEntity<List<GetChassisEstimationInformationWebDtoV1.Res>> getChassisEstimationInformation(
            @RequestParam(value = "estimationIdList", required = false) final List<Long> estimationIdList,
            @RequestParam(value = "companyType", required = false) final CompanyType companyType,
            @RequestParam(value = "chassisType", required = false) final ChassisType chassisType,
            @RequestParam(value = "startTime") final String startTime,
            @RequestParam(value = "endTime") final String endTime,
            @RequestParam(value = "limit", defaultValue = "9999999") final int limit,
            @RequestParam(value = "offset", defaultValue = "0") final int offset
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(toWebResponseObject(findChassisEstimationInformationQueryHandler.handle(
                        new FindChassisEstimationInformationQuery(
                                estimationIdList,
                                companyType,
                                chassisType,
                                convertStringToLocalDateTime(startTime),
                                convertStringToLocalDateTime(endTime).plusDays(1).minusSeconds(1),
                                limit, offset)
                )));
    }

    @GetMapping(
            value = "/estimations/count",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetCountOfChassisEstimationInformationWebDtoV1.Res> getCountOfChassisEstimationInformation(
            @RequestParam(value = "estimationIdList", required = false) final List<Long> estimationIdList,
            @RequestParam(value = "companyType", required = false) final CompanyType companyType,
            @RequestParam(value = "chassisType", required = false) final ChassisType chassisType,
            @RequestParam(value = "startTime") final String startTime,
            @RequestParam(value = "endTime") final String endTime,
            @RequestParam(value = "limit", defaultValue = "9999999") final int limit,
            @RequestParam(value = "offset", defaultValue = "0") final int offset
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new GetCountOfChassisEstimationInformationWebDtoV1.Res(
                                toWebResponseObject(
                                        findChassisEstimationInformationQueryHandler.handle(
                                                new FindChassisEstimationInformationQuery(
                                                        estimationIdList,
                                                        companyType,
                                                        chassisType,
                                                        convertStringToLocalDateTime(startTime),
                                                        convertStringToLocalDateTime(
                                                                endTime).plusDays(1)
                                                                .minusSeconds(1),
                                                        limit, offset)
                                        )).size()));
    }

    @GetMapping(
            value = "/users/estimations/count",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetAllEstimationsProvidedToCustomersWebDtoV1.Res> getAllEstimationsProvidedToCustomers() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetAllEstimationsProvidedToCustomersWebDtoV1.Res.builder()
                                .count(
                                        findAllEstimationsProvidedToCustomersQueryHandler.handle(
                                                EmptyQuery.builder().build()
                                        )
                                )
                                .build()
                );
    }

    @GetMapping(
            value = "/estimations/statistics",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetEstimatedStatisticsWebDtoV1.Res> getEstimatedStatistics(
            @Valid GetEstimatedStatisticsWebDtoV1.Req req
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetEstimatedStatisticsWebDtoV1.Res.of(
                                findEstimatedStatisticsQueryHandler.handle(
                                        FindEstimatedStatisticsQuery.Req.builder()
                                                .searchPeriodType(req.searchPeriodType())
                                                .searchPeriodValue(req.searchPeriodValue())
                                                .build()
                                )
                        )
                );
    }
}
