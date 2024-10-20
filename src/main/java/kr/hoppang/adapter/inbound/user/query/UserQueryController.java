package kr.hoppang.adapter.inbound.user.query;

import static kr.hoppang.domain.user.OauthType.APL;
import static kr.hoppang.domain.user.OauthType.KKO;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import kr.hoppang.adapter.inbound.user.webdto.LoadUserWebDtoV1;
import kr.hoppang.application.command.user.oauth.OAuthServiceAdapter;
import kr.hoppang.application.readmodel.user.handlers.LoadUserByTokenQueryHandler;
import kr.hoppang.application.readmodel.user.handlers.ValidationCheckOfPhoneNumberQueryHandler;
import kr.hoppang.application.readmodel.user.queries.LoadUserByTokenQuery;
import kr.hoppang.application.readmodel.user.queries.ValidationCheckOfPhoneNumberQuery;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserQueryController {

    private final OAuthServiceAdapter oAuthServiceAdapter;
    private final LoadUserByTokenQueryHandler loadUserByTokenCommandHandler;
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
}
