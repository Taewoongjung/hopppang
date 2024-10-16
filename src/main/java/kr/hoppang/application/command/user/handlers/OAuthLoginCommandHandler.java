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
import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
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

        /**
         * 현재는 deviceId로 유저를 찾아 와서 해당 유저가 이미 회원가입이 된 유저인지 아닌지 판별한다.
         * 이거는 웹에서는 못한다. deviceId가 없기 때문이다. 그래서 만약 웹도 확대 한다고 하면 추후 대응을 해야 할 것이다.
         * */

        // 해당 sso로 가입 된 아이디가 있으면 해당 토큰 리턴 start
        if (command.userPhoneNumber() != null) {

            User alreadyExistToken = userRepository.checkIfAlreadyLoggedIn(command.userPhoneNumber());

            if (alreadyExistToken != null) {

                // 이미 다른 소셜 계정으로 로그인을 했을 때
                duplicatedSsoLoginCheck(
                        !command.oauthType().equals(alreadyExistToken.getOauthType()) &&
                                !OauthType.NON.equals(alreadyExistToken.getOauthType()),
                        alreadyExistToken.getEmail(), alreadyExistToken.getOauthType());

                UserToken userAccessToken = alreadyExistToken.getUserTokenList().stream()
                        .filter(f -> TokenType.ACCESS.equals(f.getTokenType()))
                        .findFirst()
                        .orElseThrow(() -> new HoppangLoginException(ErrorType.NOT_EXIST_ACCESS_TOKEN));

                log.info("[핸들러 - 소셜 ({}) 로그인] 성공", command.oauthType().getType());

                return jwtUtil.createJwtForSso(
                        alreadyExistToken.getEmail(),
                        alreadyExistToken.getUserRole().name(),
                        alreadyExistToken.getOauthType().name(),
                        convertLocalDateTimeToDate(userAccessToken.getExpireIn()));
            }
        }
        // 해당 sso로 가입 된 아이디 가있으면 해당 토큰 리턴 end

        // 여기서 부터는 가입 안 된 유저이니 카카오로 부터 토큰(액세스,리프래스) 요청 하고 회원 테이블에 쌓기
        // 클라이언트로 부터 받은 code 값으로 유저 정보 파싱
        OAuthLoginResultDto oAuthLoginResult = oAuthServiceEnumMap.get(command.oauthType())
                .logIn(command.code(), command.userPhoneNumber(), command.deviceId());

        User registeredUser = signUpCommandHandler.handle(new SignUpCommand(
                oAuthLoginResult.name(),
                oAuthLoginResult.password(),
                oAuthLoginResult.email(),
                oAuthLoginResult.tel(),
                oAuthLoginResult.role(),
                oAuthLoginResult.oauthType(),
                oAuthLoginResult.deviceId(),
                oAuthLoginResult.providerUserId(),
                oAuthLoginResult.connectedAt(),
                oAuthLoginResult.accessToken(),
                oAuthLoginResult.accessTokenExpireIn(),
                oAuthLoginResult.refreshToken(),
                oAuthLoginResult.refreshTokenExpireIn(),
                command.address(),
                command.subAddress(),
                command.buildingNumber(),
                command.isPushOn()
        ));

        log.info("[핸들러 - 소셜 ({}) 로그인] 성공", command.oauthType().getType());

        // jwt 토큰을 반환한다.
        return jwtUtil.createJwtForSso(
                registeredUser.getEmail(),
                registeredUser.getUserRole().name(),
                registeredUser.getOauthType().name(),
                convertLocalDateTimeToDate(oAuthLoginResult.accessTokenExpireIn()));
    }
}
