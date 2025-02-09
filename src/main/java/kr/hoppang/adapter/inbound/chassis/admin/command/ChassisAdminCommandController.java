package kr.hoppang.adapter.inbound.chassis.admin.command;

import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.chassis.webdto.AddAndReviseChassisPriceInformationWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.AddAndReviseChassisPriceInformationWebDtoV1.Res;
import kr.hoppang.adapter.inbound.chassis.webdto.ReviseAdditionalChassisPriceWebDtoV1;
import kr.hoppang.application.command.chassis.commands.AddAndReviseChassisPriceInformationCommand;
import kr.hoppang.application.command.chassis.handlers.AddAndReviseChassisPriceInformationCommandHandler;
import kr.hoppang.application.command.chassis.handlers.ReviseChassisPriceAdditionalCriteriaCommandHandler;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin/chassis")
public class ChassisAdminCommandController {

    private final AddAndReviseChassisPriceInformationCommandHandler addAndReviseChassisPriceInformationCommandHandler;
    private final ReviseChassisPriceAdditionalCriteriaCommandHandler reviseChassisPriceAdditionalCriteriaCommandHandler;


    @PostMapping(value = "/prices")
    public ResponseEntity<Res> addAndReviseChassisPriceInformation(
            @RequestParam(value = "companyType") final CompanyType companyType,
            @RequestParam(value = "chassisType") final ChassisType chassisType,
            final @Valid @RequestBody AddAndReviseChassisPriceInformationWebDtoV1.Req req
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Res(
                        addAndReviseChassisPriceInformationCommandHandler.handle(
                                new AddAndReviseChassisPriceInformationCommand(
                                        companyType,
                                        chassisType,
                                        req.width(),
                                        req.height(),
                                        req.price()
                                )
                        )
                ));
    }

    @PutMapping(value = "/prices/additions/criteria")
    public ResponseEntity<Boolean> reviseAdditionalChassisPrice(
            @RequestBody final ReviseAdditionalChassisPriceWebDtoV1.Req req
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(reviseChassisPriceAdditionalCriteriaCommandHandler.handle(req.toCommand()));
    }
}
