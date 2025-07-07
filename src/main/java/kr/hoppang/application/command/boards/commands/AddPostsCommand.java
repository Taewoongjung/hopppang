package kr.hoppang.application.command.boards.commands;

import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

@Builder
public record AddPostsCommand(
        long boardId,
        String title,
        String contents,
        boolean isAnonymous,
        long registerId
) implements ICommand {

}
