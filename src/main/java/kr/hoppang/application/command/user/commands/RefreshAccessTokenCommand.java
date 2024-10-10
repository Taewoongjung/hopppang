package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.user.OauthType;

public record RefreshAccessTokenCommand(String expiredToken, OauthType oauthType) implements ICommand {

}
