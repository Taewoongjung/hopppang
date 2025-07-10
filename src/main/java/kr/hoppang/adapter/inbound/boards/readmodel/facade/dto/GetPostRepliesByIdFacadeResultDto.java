package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.user.User;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Builder;

public record GetPostRepliesByIdFacadeResultDto(
        List<PostsReplyFacadeDto> postsReplyList
) {

    @Builder(access = AccessLevel.PRIVATE)
    public record PostsReplyFacadeDto(
            Long id,
            Long postId,
            Long rootReplyId,
            String contents,
            Long registerId,
            String registerName,
            boolean isOwner, // 소유주 여부
            boolean isAnonymous,
            boolean hasRevised, // 수정 됐는지 여부
            LocalDateTime createdAt
    ) { }

    public static GetPostRepliesByIdFacadeResultDto of(
            final List<PostsReply> postsReplyList,
            final List<User> registerUserList,
            final Long loggedInUserId
    ) {
        return new GetPostRepliesByIdFacadeResultDto(
                postsReplyList.stream()
                        .map(
                                postsReply -> {
                                    User register = registerUserList.stream()
                                            .filter(f -> f.getId()
                                                    .equals(postsReply.getRegisterId()))
                                            .findFirst()
                                            .orElse(null);

                                    return PostsReplyFacadeDto.builder()
                                            .id(postsReply.getId())
                                            .postId(postsReply.getPostId())
                                            .rootReplyId(postsReply.getRootReplyId())
                                            .contents(postsReply.getContents())
                                            .registerId(postsReply.getRegisterId())
                                            .registerName(
                                                    register != null ? register.getName() : null
                                            )
                                            .isOwner(
                                                    loggedInUserId != null &&
                                                            loggedInUserId.equals(
                                                                    postsReply.getRegisterId())
                                            )
                                            .isAnonymous(
                                                    BoolType.convertToBoolean(
                                                            postsReply.getIsAnonymous()
                                                    )
                                            )
                                            .hasRevised(
                                                    postsReply.getCreatedAt()
                                                            .isEqual(postsReply.getLastModified())
                                            )
                                            .createdAt(postsReply.getCreatedAt())
                                            .build();
                                }
                        ).toList()
        );
    }
}
