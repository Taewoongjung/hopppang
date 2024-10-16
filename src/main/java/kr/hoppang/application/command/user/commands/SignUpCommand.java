package kr.hoppang.application.command.user.commands;

import java.time.LocalDateTime;
import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.UserRole;

public record SignUpCommand(String name,
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
                            LocalDateTime refreshTokenExpireIn,
                            String address,
                            String subAddress,
                            String buildingNumber,
                            Boolean isPushOn)
        implements ICommand {

}
