package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;

public record AddUserLoginHistoryCommand(
        long userId
) implements ICommand { }
