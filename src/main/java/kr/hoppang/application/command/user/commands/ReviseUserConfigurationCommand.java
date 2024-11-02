package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;

public record ReviseUserConfigurationCommand(long userId, boolean isPushOn) implements ICommand {

}
