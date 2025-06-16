package kr.hoppang.adapter.inbound.chassis.customer.command;

import static kr.hoppang.util.calculator.ChassisPriceCalculator.calculateSurtax;

import kr.hoppang.adapter.inbound.chassis.webdto.GetCalculatedChassisPriceWebDtoV1;
import kr.hoppang.adapter.inbound.chassis.webdto.GetCalculatedChassisPriceWebDtoV1.Res.ChassisPriceResult;
import kr.hoppang.adapter.inbound.user.AuthenticationUserId;
import kr.hoppang.application.command.chassis.commandresults.CalculateChassisPriceCommandHandlerCommandResult;
import kr.hoppang.application.command.chassis.handlers.CalculateChassisPriceCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @AuthenticationUserId final Long userId
    ) {
        req.validate();

        CalculateChassisPriceCommandHandlerCommandResult commandResult =
                calculateChassisPriceQueryHandler.handle(
                        req.toQuery(userId)
                );

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetCalculatedChassisPriceWebDtoV1.Res.builder()
                                .estimationId(commandResult.estimationId())
                                .company(commandResult.company())
                                .chassisPriceResultList(
                                        commandResult.chassisPriceResultList().stream()
                                                .map(e ->
                                                        ChassisPriceResult.builder()
                                                                .chassisType(
                                                                        e.getChassisType()
                                                                                .name()
                                                                )
                                                                .width(e.getWidth())
                                                                .height(e.getHeight())
                                                                .price(e.getPrice())
                                                                .discountedRate(
                                                                        e.getDiscountedRate()
                                                                )
                                                                .discountedPrice(
                                                                        e.getDiscountedPrice()
                                                                )
                                                                .build()
                                                ).toList()
                                )
                                .deliveryFee(commandResult.deliveryFee())
                                .demolitionFee(commandResult.demolitionFee())
                                .maintenanceFee(commandResult.maintenanceFee())
                                .ladderFee(commandResult.ladderFee())
                                .freightTransportFee(commandResult.freightTransportFee())
                                .customerFloor(commandResult.customerFloor())
                                .laborFee(commandResult.laborFee())
                                .wholeCalculatedFee(commandResult.wholeCalculatedFee())
                                .surtax(calculateSurtax(commandResult.wholeCalculatedFee()))
                                .discountedWholeCalculatedFeeAmount(
                                        commandResult.discountedWholeCalculatedFeeAmount()
                                )
                                .discountedWholeCalculatedFeeWithSurtax(
                                        commandResult.discountedWholeCalculatedFeeWithSurtax()
                                )
                                .build()
                );
    }
}
