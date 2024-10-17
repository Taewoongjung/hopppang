package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;

public record SocialSignUpFinalCommand(String userEmail,
                                       String userPhoneNumber,
                                       String address,
                                       String subAddress,
                                       String buildingNumber,
                                       Boolean isPushOn) implements ICommand {

}
