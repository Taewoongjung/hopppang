package kr.hoppang.adapter.inbound.user.query;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AUTHORIZED_USER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import kr.hoppang.adapter.inbound.user.webdto.LoadUserWebDtoV1;
import kr.hoppang.application.readmodel.user.handlers.LoadUserByTokenQueryHandler;
import kr.hoppang.application.readmodel.user.queries.LoadUserByTokenQuery;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserQueryController {

//    private final VerifyUserQueryHandler verifyUserQueryHandler;
//    private final GetUserEmailQueryHandler getUserEmailQueryHandler;
    private final LoadUserByTokenQueryHandler loadUserByTokenCommandHandler;
//    private final ValidationCheckOfEmailQueryHandler validationCheckOfEmailQueryHandler;
//    private final ValidationCheckOfPhoneNumberQueryHandler validationCheckOfPhoneNumberQueryHandler;


    @GetMapping(value = "/api/me")
    public ResponseEntity<LoadUserWebDtoV1.Res> validateUser(
            @RequestParam(name = "isAdminPage", defaultValue = "false") final boolean isAdminPage,
            final HttpServletRequest request
    ) {

        User foundUser = loadUserByTokenCommandHandler.handle(
                new LoadUserByTokenQuery(request.getHeader("authorization"), isAdminPage)
        );

        String authorities = foundUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new LoadUserWebDtoV1.Res(
                        foundUser.getUsername(),
                        foundUser.getTel(),
                        foundUser.getName(),
                        authorities
                ));
    }

//    @GetMapping(value = "/api/emails/validations")
//    public ResponseEntity<Boolean> validateEmail(
//            @RequestParam("targetEmail") final String targetEmail,
//            @RequestParam("compNumber") final String compNumber
//    ) {
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(validationCheckOfEmailQueryHandler.handle(
//                        new ValidationCheckOfEmailQuery(targetEmail, compNumber)));
//    }

//    @GetMapping(value = "/api/phones/validations")
//    public ResponseEntity<Boolean> validatePhone(
//            @RequestParam("targetPhoneNumber") final String targetPhoneNumber,
//            @RequestParam("compNumber") final String compNumber
//    ) {
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(validationCheckOfPhoneNumberQueryHandler.handle(
//                        new ValidationCheckOfPhoneNumberQuery(targetPhoneNumber, compNumber)));
//    }

//    @GetMapping(value = "/api/users/emails")
//    public ResponseEntity<GetUserEmailWebDtoV1.Res> getUserEmail(
//            @RequestParam("phoneNumber") final String phoneNumber
//    ) {
//
//        User user = getUserEmailQueryHandler.handle(new GetUserEmailQuery(phoneNumber));
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new GetUserEmailWebDtoV1.Res(user.getMaskedEmail(), user.getCreatedAt()));
//    }

//    @PostMapping(value = "/api/users/verify")
//    public ResponseEntity<Boolean> verifyUser(
//            @RequestBody final VerifyUserWebDtoV1.Req req
//    ) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                verifyUserQueryHandler.handle(new VerifyUserQuery(req.email(), req.phoneNumber())));
//    }
}
