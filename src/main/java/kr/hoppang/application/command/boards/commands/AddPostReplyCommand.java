package kr.hoppang.application.command.boards.commands;

import kr.hoppang.abstraction.domain.ICommand;
import lombok.Builder;

@Builder
public record AddPostReplyCommand(
        long postId,
        long registerId,
        String contents,
        Long rootReplyId
) implements ICommand { }
