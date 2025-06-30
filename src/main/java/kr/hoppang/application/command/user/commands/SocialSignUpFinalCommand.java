package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;

public record SocialSignUpFinalCommand(String userEmail,
                                       String userPhoneNumber,
                                       Boolean isPushOn,
                                       Boolean isAlimTalkOn) implements ICommand {

}
