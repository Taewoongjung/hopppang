package kr.hoppang.adapter.inbound.user.customer.readmodel;

import static kr.hoppang.domain.user.OauthType.APL;
import static kr.hoppang.domain.user.OauthType.GLE;
import static kr.hoppang.domain.user.OauthType.KKO;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import kr.hoppang.adapter.inbound.user.customer.webdto.GetUserConfigurationInfoWebDtoV1;
import kr.hoppang.adapter.inbound.user.customer.webdto.LoadUserWebDtoV1;
import kr.hoppang.application.command.user.oauth.OAuthServiceAdapter;
import kr.hoppang.application.readmodel.user.handlers.FindUserConfigurationInfoQueryHandler;
import kr.hoppang.application.readmodel.user.handlers.LoadUserByTokenQueryHandler;
import kr.hoppang.application.readmodel.user.handlers.ValidationCheckOfPhoneNumberQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindUserConfigurationInfoQuery;
import kr.hoppang.application.readmodel.user.queries.LoadUserByTokenQuery;
import kr.hoppang.application.readmodel.user.queries.ValidationCheckOfPhoneNumberQuery;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserConfigInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCustomerQueryController {

    private final OAuthServiceAdapter oAuthServiceAdapter;
    private final LoadUserByTokenQueryHandler loadUserByTokenCommandHandler;
    private final FindUserConfigurationInfoQueryHandler getUserConfigurationInfoQueryHandler;
    private final ValidationCheckOfPhoneNumberQueryHandler validationCheckOfPhoneNumberQueryHandler;

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
                        foundUser.getId(),
                        foundUser.getUsername(),
                        foundUser.getTel(),
                        foundUser.getName(),
                        authorities
                ));
    }

    @GetMapping(value = "/api/phones/validations")
    public ResponseEntity<Boolean> validatePhone(
            @RequestParam("targetPhoneNumber") final String targetPhoneNumber,
            @RequestParam("compNumber") final String compNumber
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(validationCheckOfPhoneNumberQueryHandler.handle(
                        new ValidationCheckOfPhoneNumberQuery(targetPhoneNumber, compNumber)));
    }

    @GetMapping(value = "/api/kakao/auth")
    public ResponseEntity<String> requestKakaoAuthBeforeSignUp() {

        return ResponseEntity.status(HttpStatus.OK).body(oAuthServiceAdapter.getReqLoginUrl(KKO));
    }

    @GetMapping(value = "/api/apple/auth")
    public ResponseEntity<String> requestAppleAuthBeforeSignUp() {

        return ResponseEntity.status(HttpStatus.OK).body(oAuthServiceAdapter.getReqLoginUrl(APL));
    }

    @GetMapping(value = "/api/google/auth")
    public ResponseEntity<String> requestGoogleAuthBeforeSignUp() {

        return ResponseEntity.status(HttpStatus.OK).body(oAuthServiceAdapter.getReqLoginUrl(GLE));
    }

    @GetMapping(value = "/api/users/{userId}/configs")
    public ResponseEntity<GetUserConfigurationInfoWebDtoV1.Res> getUserConfigurationInfo(
        @PathVariable(value = "userId") final long userId
    ) {

        UserConfigInfo userConfigInfo = getUserConfigurationInfoQueryHandler.handle(
                new FindUserConfigurationInfoQuery(userId));

        return ResponseEntity.status(HttpStatus.OK).body(
                new GetUserConfigurationInfoWebDtoV1.Res(userConfigInfo.getIsPushOnAsBoolean()));
    }
}
