package kr.hoppang.application.command.boards.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_ALLOWED_USER_DELETING_POST_REPLY;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_TARGET_POST_REPLY;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.List;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.common.exception.custom.HoppangException;
import kr.hoppang.application.command.boards.commands.DeletePostsReplyCommand;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.PostsReplyCommandRepository;
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletePostsReplyCommandHandler implements ICommandHandler<DeletePostsReplyCommand, Boolean> {

    private final PostsReplyQueryRepository postsReplyQueryRepository;
    private final PostsReplyCommandRepository postsReplyCommandRepository;



    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Boolean handle(final DeletePostsReplyCommand command) {

        List<PostsReply> replyList = postsReplyQueryRepository.findPostsReplyByPostId(
                command.postId());

        PostsReply deletingReply = replyList.stream()
                .filter(reply -> reply.getId().equals(command.replyId()))
                .findFirst()
                .orElseThrow(() -> new HoppangException(NOT_EXIST_TARGET_POST_REPLY));

        check(!deletingReply.getRegisterId().equals(command.deleterId()),
                NOT_ALLOWED_USER_DELETING_POST_REPLY);

        postsReplyCommandRepository.delete(deletingReply.getId());

        return true;
    }
}
