package kr.hoppang.adapter.inbound.user.command;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.user.webdto.RequestValidationWebDtoV1;
import kr.hoppang.adapter.inbound.user.webdto.SignUpDtoWebDtoV1;
import kr.hoppang.adapter.inbound.user.webdto.SocialSignUpFinalWebDtoV1;
import kr.hoppang.adapter.inbound.user.webdto.SsoSignUpWebDtoV1;
import kr.hoppang.adapter.inbound.user.webdto.SsoSignUpWebDtoV1.Res;
import kr.hoppang.adapter.inbound.user.webdto.UpdateUserConfigWebDtoV1;
import kr.hoppang.application.command.user.commandresults.OAuthLoginCommandResult;
import kr.hoppang.application.command.user.commands.OAuthLoginCommand;
import kr.hoppang.application.command.user.commands.RefreshAccessTokenCommand;
import kr.hoppang.application.command.user.commands.ReviseUserConfigurationCommand;
import kr.hoppang.application.command.user.commands.SendPhoneValidationSmsCommand;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.commands.SocialSignUpFinalCommand;
import kr.hoppang.application.command.user.handlers.OAuthLoginCommandHandler;
import kr.hoppang.application.command.user.handlers.RefreshAccessTokenCommandHandler;
import kr.hoppang.application.command.user.handlers.ReviseUserConfigurationCommandHandler;
import kr.hoppang.application.command.user.handlers.SendPhoneValidationSmsCommandHandler;
import kr.hoppang.application.command.user.handlers.SignUpCommandHandler;
import kr.hoppang.application.command.user.handlers.SocialSignUpFinalCommandHandler;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCommandController {

    private final SignUpCommandHandler signUpCommandHandler;
    private final OAuthLoginCommandHandler oAuthLoginCommandHandler;
    private final SocialSignUpFinalCommandHandler socialSignUpFinalCommandHandler;
    private final RefreshAccessTokenCommandHandler refreshAccessTokenCommandHandler;
    private final SendPhoneValidationSmsCommandHandler sendPhoneValidationSmsCommandHandler;
    private final ReviseUserConfigurationCommandHandler reviseUserConfigurationCommandHandler;

    @PostMapping(value = "/api/signup")
    public ResponseEntity<SignUpDtoWebDtoV1.Res> signup(
            final @Valid @RequestBody SignUpDtoWebDtoV1.Req req) {

        User justRegisteredUser = signUpCommandHandler.handle(
                new SignUpCommand(
                        req.name(),
                        req.password(),
                        req.email(),
                        req.tel(),
                        req.role(),
                        req.oauthType(),
                        req.deviceId(),
                        req.deviceType(),
                        null, null, null, null, null, null
                )
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SignUpDtoWebDtoV1.Res(
                        true,
                        justRegisteredUser.getName()
                ));
    }

    @PutMapping(value = "/api/social/users")
    public ResponseEntity<SocialSignUpFinalWebDtoV1.Res> socialSignUpFinal(
            final @RequestBody SocialSignUpFinalWebDtoV1.Req req) {

        String email = socialSignUpFinalCommandHandler.handle(new SocialSignUpFinalCommand(
                req.userEmail(),
                req.userPhoneNumber(),
                req.address(),
                req.subAddress(),
                req.buildingNumber(),
                req.isPushOn()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(new SocialSignUpFinalWebDtoV1.Res(email));
    }

    @PostMapping(value = "/api/kakao/signup/{code}")
    public ResponseEntity<SsoSignUpWebDtoV1.Res> kakaoSignUp(
            @PathVariable(value = "code") final String code,
            @RequestBody final SsoSignUpWebDtoV1.Req req,
            HttpServletResponse response) throws Exception {

        log.info("카카오 로그인 = {}", code);

        OAuthLoginCommandResult oAuthLoginCommandResult = oAuthLoginCommandHandler.handle(
                new OAuthLoginCommand(
                        code,
                        req.deviceId(),
                        req.deviceType(),
                        OauthType.KKO
                ));

        response.addHeader("Authorization", "Bearer " + oAuthLoginCommandResult.jwt());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Res(
                        true,
                        oAuthLoginCommandResult.isTheFirstLogIn(),
                        oAuthLoginCommandResult.userEmail(),
                        OauthType.KKO));
    }

    @PutMapping(value = "/api/kakao/refresh")
    public ResponseEntity<Boolean> kakaoLoginRefreshAccessTokenWhenExpired(
            @RequestParam(value = "expiredToken") final String expiredToken,
            HttpServletResponse response
    ) throws Exception {

        String jwt = refreshAccessTokenCommandHandler.handle(
                new RefreshAccessTokenCommand(expiredToken, OauthType.KKO));

        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PostMapping(value = "/api/apple/signup/{code}")
    public ResponseEntity<SsoSignUpWebDtoV1.Res> appleSignUp(
            @PathVariable(value = "code") final String code,
            @RequestBody final SsoSignUpWebDtoV1.Req req,
            HttpServletResponse response) throws Exception {

        log.info("애플 로그인 = {}", code);

        OAuthLoginCommandResult oAuthLoginCommandResult = oAuthLoginCommandHandler.handle(
                new OAuthLoginCommand(
                        code,
                        req.deviceId(),
                        req.deviceType(),
                        OauthType.APL
                ));

        response.addHeader("Authorization", "Bearer " + oAuthLoginCommandResult.jwt());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Res(
                        true,
                        oAuthLoginCommandResult.isTheFirstLogIn(),
                        oAuthLoginCommandResult.userEmail(),
                        OauthType.APL));
    }

    @PutMapping(value = "/api/apple/refresh")
    public ResponseEntity<Boolean> appleLoginRefreshAccessTokenWhenExpired(
            @RequestParam(value = "expiredToken") final String expiredToken,
            HttpServletResponse response
    ) throws Exception {

        String jwt = refreshAccessTokenCommandHandler.handle(
                new RefreshAccessTokenCommand(expiredToken, OauthType.APL));

        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PostMapping(value = "/api/phones/validations")
    public ResponseEntity<Boolean> requestValidationPhone(
            @RequestBody final RequestValidationWebDtoV1.PhoneValidationReq req
    ) throws Exception {

        sendPhoneValidationSmsCommandHandler.handle(
                new SendPhoneValidationSmsCommand(req.email(), req.targetPhoneNumber(),
                        req.validationType()));

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PutMapping(value = "/api/users/{userId}/configs")
    public ResponseEntity<Boolean> updateUserConfig(
            @PathVariable(value = "userId") final Long userId,
            @RequestBody final UpdateUserConfigWebDtoV1.Req req
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(reviseUserConfigurationCommandHandler.handle(
                        new ReviseUserConfigurationCommand(userId, req.isPushOn())));
    }
}
