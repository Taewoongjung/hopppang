package kr.hoppang.application.command.boards.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.boards.commands.AddPostsCommand;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsCommandRepository;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddPostsCommandHandler implements ICommandHandler<AddPostsCommand, Long> {

    private final PostsCommandRepository postsCommandRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    public Long handle(final AddPostsCommand command) {

        return postsCommandRepository.create(
                Posts.builder()
                        .boardId(command.boardId())
                        .title(command.title())
                        .contents(command.contents())
                        .isAnonymous(
                                BoolType.convertBooleanToType(command.isAnonymous())
                        )
                        .registerId(command.registerId())
                        .isDeleted(BoolType.F)
                        .build()
        );
    }
}
