package kr.hoppang.application.command.boards.commands;

import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

@Builder
public record DeletePostsCommand(
        long postId,
        long deleterId
) implements ICommand { }