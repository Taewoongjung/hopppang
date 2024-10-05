package kr.hoppang.adapter.inbound.user.command;

import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.user.webdto.SignUpDtoWebDtoV1;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.handlers.SignUpCommandHandler;
import kr.hoppang.util.auth.kakao.KakaoAuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCommandController {

    private final KakaoAuthUtil kakaoAuthUtil;
    private final SignUpCommandHandler signUpCommandHandler;

    @PostMapping(value = "/api/signup")
    public ResponseEntity<SignUpDtoWebDtoV1.Res> signup(
            final @Valid @RequestBody SignUpDtoWebDtoV1.Req req) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SignUpDtoWebDtoV1.Res(
                        true,
                        signUpCommandHandler.handle(
                                new SignUpCommand(
                                        req.name(),
                                        req.password(),
                                        req.email(),
                                        req.tel(),
                                        req.role(),
                                        req.oauthType(),
                                        req.token(),
                                        req.deviceId()
                                )
                        )));
    }

    @PostMapping(value = "/api/kakao/auth")
    public ResponseEntity<String> requestKakaoAuthBeforeSignUp() {

        return ResponseEntity.status(HttpStatus.OK).body(kakaoAuthUtil.getReqLoginUrl());
    }

    @PostMapping(value = "/api/kakao/signup/{code}")
    public ResponseEntity<String> kakaoSignUp(@PathVariable(value = "code") final String code) {
        log.info("카카오 로그인 = {}", code);

        return ResponseEntity.status(HttpStatus.OK).body(code);
    }
}
