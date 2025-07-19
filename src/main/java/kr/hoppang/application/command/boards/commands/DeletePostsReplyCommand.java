package kr.hoppang.application.command.boards.commands;

import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

@Builder
public record DeletePostsReplyCommand(
        long postId,
        long replyId,
        long deleterId
) implements ICommand { }
