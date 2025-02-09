package kr.hoppang.adapter.inbound.chassis.customer.readmodel;

import kr.hoppang.application.readmodel.chassis.handlers.InquiryChassisEstimation;
import kr.hoppang.application.readmodel.chassis.queries.InquiryChassisEstimationCommand;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/customers/chassis")
public class ChassisCustomerQueryController {

    private final InquiryChassisEstimation inquiryChassisEstimation;


    @GetMapping(path = "")
    public ResponseEntity<Object> getAllChassisInformationOfSingleUser(
            @AuthenticationPrincipal final User user
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        null
                );
    }

    @GetMapping(path = "/estimations/{estimationId}/inquiries")
    public ResponseEntity<Boolean> inquiryChassisEstimation(
            @PathVariable(value = "estimationId") final long estimationId
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(inquiryChassisEstimation.handle(
                        new InquiryChassisEstimationCommand(estimationId)));
    }
}
