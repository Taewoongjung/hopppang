package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostRepliesByIdFacadeResultDto;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetPostRepliesByIdFacade {

    private final UserRepository userRepository;
    private final PostsReplyQueryRepository postsReplyQueryRepository;


    public GetPostRepliesByIdFacadeResultDto query(
            final long postId,
            final Long loggedInUserId
    ) {

        List<PostsReply> postsReplyList = postsReplyQueryRepository.findPostsReplyByPostId(postId);

        List<User> registerUserList = userRepository.findAllUsersIdIn(
                postsReplyList.stream()
                        .map(PostsReply::getRegisterId)
                        .distinct()
                        .toList()
        );

        return GetPostRepliesByIdFacadeResultDto.of(
                postsReplyList,
                registerUserList,
                loggedInUserId
        );
    }
}