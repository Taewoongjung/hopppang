package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.domain.user.OauthType;

public record OAuthLoginCommand(String code,
                                String deviceId,
                                String userPhoneNumber,
                                String address,
                                String subAddress,
                                String buildingNumber,
                                Boolean isPushOn,
                                OauthType oauthType) implements ICommand {

}
