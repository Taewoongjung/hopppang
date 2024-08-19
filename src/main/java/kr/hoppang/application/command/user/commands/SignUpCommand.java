package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.UserRole;

public record SignUpCommand(String name,
                            String password,
                            String email,
                            String tel,
                            UserRole role,
                            OauthType oauthType,
                            String token,
                            String deviceId)
        implements ICommand {

}
