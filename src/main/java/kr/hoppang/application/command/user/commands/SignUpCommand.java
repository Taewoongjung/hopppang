package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.user.SsoType;
import kr.hoppang.domain.user.UserRole;

public record SignUpCommand(String name,
                            String password,
                            String email,
                            String tel,
                            UserRole role,
                            SsoType ssoType)
        implements ICommand {

}
