package kr.hoppang.application.command.user.oauth;

import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import kr.hoppang.application.command.user.oauth.dto.OAuthLoginResultDto;
import kr.hoppang.application.command.user.oauth.dto.OAuthServiceLogInResultDto;
import kr.hoppang.domain.user.OauthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthServiceAdapter {

    private final List<OAuthService> oAuthServices;

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

    public String getReqLoginUrl(final OauthType oauthType) {
        return oAuthServiceEnumMap.get(oauthType).getReqLoginUrl();
    }

    public OAuthLoginResultDto logIn(final OauthType oauthType, final String code) {
        return oAuthServiceEnumMap.get(oauthType).logIn(code);
    }

    public OAuthServiceLogInResultDto refreshAccessToken(final OauthType oauthType,
            final String userEmail) {
        return oAuthServiceEnumMap.get(oauthType).refreshAccessToken(userEmail);
    }
}