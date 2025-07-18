package kr.hoppang.application.command.boards.commands;

import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

@Builder
public record RevisePostsCommand(
        long postId,
        long boardId,
        String title,
        String contents,
        boolean isAnonymous,
        long revisingUserId
) implements ICommand { }
