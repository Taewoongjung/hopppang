package kr.hoppang.adapter.inbound.chassis.command;

import kr.hoppang.adapter.inbound.chassis.webdto.GetCalculatedChassisPriceWebDtoV1;
import kr.hoppang.application.command.chassis.handlers.CalculateChassisPriceCommandHandler;
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
public class ChassisCustomerCommandController {

    private final CalculateChassisPriceCommandHandler calculateChassisPriceQueryHandler;


    @PostMapping(value = "/calculations/prices")
    public ResponseEntity<GetCalculatedChassisPriceWebDtoV1.Res> getCalculatedChassisPrice(
            @RequestBody final GetCalculatedChassisPriceWebDtoV1.Req req,
            @AuthenticationPrincipal final User user
    ) {
        req.validate();

        return ResponseEntity.status(HttpStatus.OK)
                .body(GetCalculatedChassisPriceWebDtoV1.Res.of(
                        calculateChassisPriceQueryHandler.handle(req.toQuery(user))));
    }
}
