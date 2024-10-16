package kr.hoppang.application.command.user.oauth.dto;

import java.time.LocalDateTime;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.UserRole;

public record OAuthLoginResultDto(String name,
                                  String password,
                                  String email,
                                  String tel,
                                  UserRole role,
                                  OauthType oauthType,
                                  String deviceId,
                                  String providerUserId,
                                  LocalDateTime connectedAt,
                                  String accessToken,
                                  LocalDateTime accessTokenExpireIn,
                                  String refreshToken,
                                  LocalDateTime refreshTokenExpireIn) {

}
