package kr.hoppang.application.command.user.oauth.dto;

import java.util.Date;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.UserRole;

public record OAuthServiceLogInResultDto(String email,
                                         UserRole userRole,
                                         OauthType oauthType,
                                         Date accessTokenExpireIn) {

}
