package kr.hoppang.adapter.inbound.user.command;

import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.user.webdto.SignUpDtoWebDtoV1;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.handlers.SignUpCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCommandController {

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
                                        req.ssoType()
                                )
                        )));
    }
}
