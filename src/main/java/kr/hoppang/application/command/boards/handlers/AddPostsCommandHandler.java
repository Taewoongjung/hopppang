package kr.hoppang.application.command.boards.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.PostsCommandRepositoryJpaAdapter;
import kr.hoppang.application.command.boards.commands.AddPostsCommand;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddPostsCommandHandler implements ICommandHandler<AddPostsCommand, Boolean> {

    private final PostsCommandRepositoryJpaAdapter postsRepositoryCommandAdapter;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Boolean handle(final AddPostsCommand command) {

        postsRepositoryCommandAdapter.create(
                Posts.builder()
                        .boardId(command.boardId())
                        .title(command.title())
                        .contents(command.contents())
                        .isAnonymous(
                                BoolType.convertBooleanToType(command.isAnonymous())
                        )
                        .registerId(command.registerId())
                        .build()
        );

        return true;
    }
}
