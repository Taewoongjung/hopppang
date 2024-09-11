package kr.hoppang.adapter.inbound.chassis.command;

import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.chassis.webdto.AddAndReviseChassisPriceInformationWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.AddAndReviseChassisPriceInformationWebDtoV1.Res;
import kr.hoppang.application.command.chassis.commands.AddAndReviseChassisPriceInformationCommand;
import kr.hoppang.application.command.chassis.handlers.AddAndReviseChassisPriceInformationCommandHandler;
import kr.hoppang.application.readmodel.chassis.queries.FindChassisPriceInfoByCompanyTypeQuery;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChassisCommandController {

    private final AddAndReviseChassisPriceInformationCommandHandler addAndReviseChassisPriceInformationCommandHandler;

    @PostMapping(value = "/api/chassis/prices")
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
}
