package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

@Builder
public record AddUserLoginHistoryCommand(
        long userId
) implements ICommand { }
