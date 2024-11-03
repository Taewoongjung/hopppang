package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.user.OauthType;

public record WithdrawUserCommand(long userId, OauthType oauthType) implements ICommand {

}
