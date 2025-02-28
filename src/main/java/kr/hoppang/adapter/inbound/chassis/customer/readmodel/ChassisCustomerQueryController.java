package kr.hoppang.adapter.inbound.chassis.customer.readmodel;

import kr.hoppang.adapter.inbound.chassis.webdto.GetAllChassisInformationOfSingleUserWebDtoV1;
import kr.hoppang.adapter.inbound.user.AuthenticationUserId;
import kr.hoppang.application.readmodel.chassis.handlers.FindAllChassisInformationOfSingleUserQueryHandler;
import kr.hoppang.application.command.chassis.handlers.InquiryChassisEstimationCommandHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindAllChassisInformationOfSingleUserQuery;
import kr.hoppang.application.command.chassis.commands.InquiryChassisEstimationCommand;
import kr.hoppang.application.readmodel.chassis.queryresults.FindAllChassisInformationOfSingleUserQueryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/customers/chassis")
public class ChassisCustomerQueryController {

    private final InquiryChassisEstimationCommandHandler inquiryChassisEstimation;
    private final FindAllChassisInformationOfSingleUserQueryHandler findAllChassisInformationOfSingleUserQueryHandler;


    @GetMapping(path = "")
    public ResponseEntity<GetAllChassisInformationOfSingleUserWebDtoV1.Response> getAllChassisInformationOfSingleUser(
            @RequestParam(value = "lastEstimationId", required = false) final Long lastEstimationId,
            @PageableDefault(size = 5) final Pageable pageable,
            @AuthenticationUserId final Long userId
    ) {

        FindAllChassisInformationOfSingleUserQueryResult result = findAllChassisInformationOfSingleUserQueryHandler.handle(
                FindAllChassisInformationOfSingleUserQuery.builder()
                        .userId(userId)
                        .pageable(pageable)
                        .lastEstimationId(lastEstimationId)
                        .build()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetAllChassisInformationOfSingleUserWebDtoV1.Response.of(
                                result.chassisEstimationInfoList(),
                                result.lastEstimationId(),
                                result.isEndOfList()
                        )
                );
    }

    @GetMapping(path = "/estimations/{estimationId}/inquiries")
    public ResponseEntity<Boolean> inquiryChassisEstimation(
            @PathVariable(value = "estimationId") final long estimationId,
            @AuthenticationUserId final Long userId
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(inquiryChassisEstimation.handle(
                        new InquiryChassisEstimationCommand(estimationId, userId)));
    }
}
