package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.user.OauthType;

public record OAuthLoginCommand(String code,
                                String deviceId,
                                OauthType oauthType) implements ICommand {

}
