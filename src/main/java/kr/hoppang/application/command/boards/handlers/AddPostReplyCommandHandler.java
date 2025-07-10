package kr.hoppang.application.command.boards.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.boards.commands.AddPostReplyCommand;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.PostsReplyCommandRepository;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddPostReplyCommandHandler implements ICommandHandler<AddPostReplyCommand, Long> {

    private final PostsReplyCommandRepository postsReplyCommandRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    public Long handle(final AddPostReplyCommand command) {

        return postsReplyCommandRepository.create(
                PostsReply.builder()
                        .postId(command.postId())
                        .rootReplyId(command.rootReplyId())
                        .contents(command.contents())
                        .registerId(command.registerId())
                        .isAnonymous(BoolType.F)
                        .isDeleted(BoolType.F)
                        .build()
        );
    }
}
