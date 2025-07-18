package kr.hoppang.application.command.boards.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_ALLOWED_USER_REVISING_POST_REPLY;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.List;
import java.util.NoSuchElementException;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.boards.commands.RevisePostsReplyCommand;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.PostsReplyCommandRepository;
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RevisePostsReplyCommandHandler implements ICommandHandler<RevisePostsReplyCommand, Boolean> {

    private final PostsReplyQueryRepository postsReplyQueryRepository;
    private final PostsReplyCommandRepository postsReplyCommandRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Boolean handle(final RevisePostsReplyCommand command) {

        List<PostsReply> postsReplyList = postsReplyQueryRepository.findPostsReplyByPostId(
                command.postId());

        PostsReply postsReply = postsReplyList.stream()
                .filter(reply -> command.replyId() == reply.getId())
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 댓글입니다."));

        check(!postsReply.getRegisterId().equals(command.revisingUserId()),
                NOT_ALLOWED_USER_REVISING_POST_REPLY);

        postsReply.setContents(command.contents());

        return postsReplyCommandRepository.revise(postsReply);
    }
}
