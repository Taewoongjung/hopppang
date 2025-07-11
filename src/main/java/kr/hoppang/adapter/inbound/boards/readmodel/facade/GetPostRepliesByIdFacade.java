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
            final long postId
    ) {

        List<PostsReply> allPostsReplyList = postsReplyQueryRepository.findPostsReplyByPostId(postId);

        List<User> registerUserList = userRepository.findAllUsersIdIn(
                allPostsReplyList.stream()
                        .map(PostsReply::getRegisterId)
                        .distinct()
                        .toList()
        );

        List<PostsReply> postsRootReplyList = allPostsReplyList.stream()
                .filter(PostsReply::isParent)
                .toList();

        List<PostsReply> postsBranchReplyList = allPostsReplyList.stream()
                .filter(f -> !f.isParent())
                .toList();

        return GetPostRepliesByIdFacadeResultDto.of(
                postsRootReplyList,
                postsBranchReplyList,
                registerUserList
        );
    }
}