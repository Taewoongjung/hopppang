package kr.hoppang.application.command.boards.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_ALLOWED_USER_REVISING_POST;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_REVISED_TARGET_POST;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.boards.commands.RevisePostsCommand;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsCommandRepository;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RevisePostsCommandHandler implements ICommandHandler<RevisePostsCommand, Boolean> {

    private final PostsQueryRepository postsQueryRepository;
    private final PostsCommandRepository postsCommandRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Boolean handle(final RevisePostsCommand command) {

        Posts originalPosts = postsQueryRepository.findPostsByPostId(command.postId());

        check(originalPosts == null, NOT_EXIST_REVISED_TARGET_POST);

        check(!originalPosts.getRegisterId().equals(command.revisingUserId()),
                NOT_ALLOWED_USER_REVISING_POST);

        Posts revisedPosts = Posts.builder()
                .id(originalPosts.getId())
                .boardId(command.boardId())
                .registerId(originalPosts.getRegisterId())
                .title(command.title())
                .contents(command.contents())
                .isAnonymous(BoolType.convertBooleanToType(command.isAnonymous()))
                .isDeleted(BoolType.F)
                .build();

        return postsCommandRepository.revise(revisedPosts);
    }
}
