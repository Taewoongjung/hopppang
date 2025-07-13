package kr.hoppang.adapter.inbound.boards.readmodel.facade;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostRepliesByIdFacadeResultDto;
import kr.hoppang.application.readmodel.boards.hanlders.FindPostsRepliesCountsOfLikesByIdsQueryHandler;
import kr.hoppang.application.readmodel.boards.hanlders.FindPostsRepliesLikeClickedByIdsQueryHandler;
import kr.hoppang.application.readmodel.boards.queries.FindPostsRepliesCountsOfLikesByIdsQuery;
import kr.hoppang.application.readmodel.boards.queries.FindPostsRepliesLikeClickedByIdsQuery;
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
    private final FindPostsRepliesLikeClickedByIdsQueryHandler findPostsRepliesLikeClickedByIdsQueryHandler;
    private final FindPostsRepliesCountsOfLikesByIdsQueryHandler findPostsRepliesCountsOfLikesByIdsQueryHandler;


    public GetPostRepliesByIdFacadeResultDto query(
            final long postId,
            final Long loggedInUserId
    ) {

        List<PostsReply> allPostsReplyList = postsReplyQueryRepository.findPostsReplyByPostId(
                postId
        );

        if (allPostsReplyList.isEmpty()) {
            return new GetPostRepliesByIdFacadeResultDto(Collections.emptyList());
        }

        List<User> registerUserList = userRepository.findAllUsersIdIn(
                allPostsReplyList.stream()
                        .map(PostsReply::getRegisterId)
                        .distinct()
                        .toList()
        );

        if (loggedInUserId != null) {
            List<Long> likedReplyIdList = findPostsRepliesLikeClickedByIdsQueryHandler.handle(
                    FindPostsRepliesLikeClickedByIdsQuery.builder()
                            .replyIdList(
                                    allPostsReplyList.stream()
                                            .map(PostsReply::getId)
                                            .toList()
                            )
                            .loggedUserId(loggedInUserId)
                            .build()
            );

            allPostsReplyList.stream()
                    .filter(f -> likedReplyIdList.contains(f.getId()))
                    .forEach(PostsReply::liked);
        }

        Map<Long, Long> countsOfReplies = findPostsRepliesCountsOfLikesByIdsQueryHandler.handle(
                FindPostsRepliesCountsOfLikesByIdsQuery.builder()
                        .replyIdList(
                                allPostsReplyList.stream()
                                        .map(PostsReply::getId)
                                        .toList()
                        )
                        .build()
        ).countDatas();

        for (PostsReply reply : allPostsReplyList) {
            Long likeCount = countsOfReplies.get(reply.getId());
            if (likeCount != null) {
                reply.setLikeCount(likeCount);
            }
        }

        List<PostsReply> postsRootReplyList = allPostsReplyList.stream()
                .filter(PostsReply::isParent)
                .toList();

        List<PostsReply> postsBranchReplyList = allPostsReplyList.stream()
                .filter(PostsReply::isChild)
                .toList();

        return GetPostRepliesByIdFacadeResultDto.of(
                postsRootReplyList,
                postsBranchReplyList,
                registerUserList
        );
    }
}