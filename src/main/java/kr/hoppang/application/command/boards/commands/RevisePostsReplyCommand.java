package kr.hoppang.application.command.boards.commands;

import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

@Builder
public record RevisePostsReplyCommand(
        long postId,
        long replyId,
        String contents,
        long revisingUserId
) implements ICommand { }