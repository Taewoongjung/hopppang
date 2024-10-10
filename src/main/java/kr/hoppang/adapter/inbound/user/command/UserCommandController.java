package kr.hoppang.adapter.inbound.user.command;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.user.webdto.SignUpDtoWebDtoV1;
import kr.hoppang.application.command.user.commands.OAuthLoginCommand;
import kr.hoppang.application.command.user.commands.RefreshAccessTokenCommand;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.handlers.OAuthLoginCommandHandler;
import kr.hoppang.application.command.user.handlers.RefreshAccessTokenCommandHandler;
import kr.hoppang.application.command.user.handlers.SignUpCommandHandler;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import kr.hoppang.util.auth.kakao.KakaoAuthUtil;
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

    private final KakaoAuthUtil kakaoAuthUtil;
    private final SignUpCommandHandler signUpCommandHandler;
    private final OAuthLoginCommandHandler oAuthLoginCommandHandler;
    private final RefreshAccessTokenCommandHandler refreshAccessTokenCommandHandler;

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
                        null, null, null, null, null, null
                )
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SignUpDtoWebDtoV1.Res(
                        true,
                        justRegisteredUser.getName()
                ));
    }

    @PostMapping(value = "/api/kakao/auth")
    public ResponseEntity<String> requestKakaoAuthBeforeSignUp() {

        return ResponseEntity.status(HttpStatus.OK).body(kakaoAuthUtil.getReqLoginUrl());
    }

    @PostMapping(value = "/api/kakao/signup/{code}")
    public ResponseEntity<Boolean> kakaoSignUp(
            @PathVariable(value = "code") final String code,
            @RequestParam(value = "deviceId") final String deviceId,
            HttpServletResponse response) {

        log.info("카카오 로그인 = {}", code);

        // 토큰이 만료 됐는지 검증하는 핸들러

        String jwt = oAuthLoginCommandHandler.handle(new OAuthLoginCommand(code, deviceId ,
                OauthType.KKO));

        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PutMapping(value = "/api/kakao/refresh")
    public ResponseEntity<Boolean> kakaoLoginRefreshAccessTokenWhenExpired(
            @RequestParam(value = "expiredToken") final String expiredToken,
            HttpServletResponse response
    ) {

        String jwt = refreshAccessTokenCommandHandler.handle(
                new RefreshAccessTokenCommand(expiredToken, OauthType.KKO));

        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

}
