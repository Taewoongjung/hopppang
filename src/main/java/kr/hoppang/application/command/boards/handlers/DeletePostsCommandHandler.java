package kr.hoppang.application.command.boards.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_ALLOWED_USER_DELETING_POST_REPLY;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_TARGET_POST;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.boards.commands.DeletePostsCommand;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsCommandRepository;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePostsCommandHandler implements ICommandHandler<DeletePostsCommand, Boolean> {

    private final PostsQueryRepository postsQueryRepository;
    private final PostsCommandRepository postsCommandRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    public Boolean handle(final DeletePostsCommand command) {
        Posts deletingPosts = postsQueryRepository.findPostsByPostId(command.postId());

        check(deletingPosts == null, NOT_EXIST_TARGET_POST);

        check(!deletingPosts.getRegisterId().equals(command.deleterId()),
                NOT_ALLOWED_USER_DELETING_POST_REPLY);

        postsCommandRepository.delete(command.postId());

        return true;
    }
}
