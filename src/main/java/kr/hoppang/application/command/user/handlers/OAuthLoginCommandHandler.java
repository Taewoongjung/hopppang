package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.util.CheckUtil.duplicatedSsoLoginCheck;
import static kr.hoppang.adapter.common.util.VersatileUtil.convertLocalDateTimeToDate;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.common.exception.ErrorType;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.application.command.user.commands.OAuthLoginCommand;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.application.command.user.oauth.OAuthService;
import kr.hoppang.config.security.jwt.JWTUtil;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.TokenType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserToken;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthLoginCommandHandler implements ICommandHandler<OAuthLoginCommand, String> {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final List<OAuthService> oAuthServices;
    private final SignUpCommandHandler signUpCommandHandler;

    private EnumMap<OauthType, OAuthService> oAuthServiceEnumMap;

    @PostConstruct
    void init() {
        oAuthServiceEnumMap = oAuthServices.stream()
                .collect(Collectors.toMap(
                        OAuthService::getOauthType,
                        (oAuthService) -> oAuthService,
                        (existService, newService) -> existService,
                        () -> new EnumMap<>(OauthType.class)
                ));
    }


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public String handle(final OAuthLoginCommand command) {

        log.info("[핸들러 - 소셜 ({}) 로그인] OAuthLoginCommand = {}", command.oauthType().getType(),
                command);

        // 해당 sso로 가입 된 아이디 가있으면 해당 토큰 리턴 start
        User alreadyExistToken = userRepository.checkIfAlreadyLoggedIn(command.deviceId());

        // 이미 다른 소셜 계정으로 로그인을 했을 때
        duplicatedSsoLoginCheck(!command.oauthType().equals(alreadyExistToken.getOauthType()),
                alreadyExistToken.getEmail(), alreadyExistToken.getOauthType());

        if (alreadyExistToken != null) {
            log.info("[핸들러 - 소셜 ({}) 로그인] 성공", command.oauthType().getType());

            UserToken userAccessToken = alreadyExistToken.getUserTokenList().stream()
                    .filter(f -> TokenType.ACCESS.equals(f.getTokenType()))
                    .findFirst()
                    .orElseThrow(() -> new HoppangLoginException(ErrorType.NOT_EXIST_ACCESS_TOKEN));

            return jwtUtil.createJwtForSso(
                    alreadyExistToken.getEmail(),
                    alreadyExistToken.getUserRole().name(),
                    alreadyExistToken.getOauthType().name(),
                    convertLocalDateTimeToDate(userAccessToken.getExpireIn()));
        }
        // 해당 sso로 가입 된 아이디 가있으면 해당 토큰 리턴 end

        // 여기서 부터는 가입 안 된 유저이니 카카오로 부터 토큰(액세스,리프래스) 요청 하고 회원 테이블에 쌓기
        // 클라이언트로 부터 받은 code 값으로 유저 정보 파싱
        SignUpCommand signUpCommand = oAuthServiceEnumMap.get(command.oauthType())
                .logIn(command.code(), command.deviceId());

        User registeredUser = signUpCommandHandler.handle(signUpCommand);

        log.info("[핸들러 - 소셜 ({}) 로그인] 성공", command.oauthType().getType());

        // jwt 토큰을 반환한다.
        return jwtUtil.createJwtForSso(
                registeredUser.getEmail(),
                registeredUser.getUserRole().name(),
                registeredUser.getOauthType().name(),
                convertLocalDateTimeToDate(signUpCommand.accessTokenExpireIn()));
    }
}
