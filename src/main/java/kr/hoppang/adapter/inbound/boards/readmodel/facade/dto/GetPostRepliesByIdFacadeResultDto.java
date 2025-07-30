package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.user.User;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public record GetPostRepliesByIdFacadeResultDto(
        List<PostsRootReplyFacadeDto> postsReplyList
) {

    @Getter
    @Builder
    public static class PostsRootReplyFacadeDto {
        private Long id;
        private Long postId;
        private String contents;
        private Long registerId;
        private String registerName;
        private boolean anonymous;
        private boolean revised;
        private boolean deleted;
        private long likes;
        private boolean isLiked;
        private LocalDateTime createdAt;

        @Setter(AccessLevel.PRIVATE)
        private List<PostsBranchReplyFacadeDto> postsChildReplyList;
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record PostsBranchReplyFacadeDto(
            Long id,
            Long postId,
            Long rootReplyId,
            String contents,
            Long registerId,
            String registerName,
            boolean anonymous,
            boolean revised,
            boolean deleted,
            long likes,
            boolean isLiked,
            LocalDateTime createdAt
    ) {}

    public static GetPostRepliesByIdFacadeResultDto of(
            final List<PostsReply> rootReplies,
            final List<PostsReply> branchReplies,
            final List<User> users
    ) {
        // 1. 사용자 매핑
        Map<Long, String> userIdToName = users.stream()
                .collect(Collectors.toMap(User::getId, User::getName));

        // 2. 자식 댓글을 rootReplyId 기준으로 그룹핑
        Map<Long, List<PostsBranchReplyFacadeDto>> groupedBranchReplies = branchReplies.stream()
                .map(reply -> toBranchDto(reply, userIdToName))
                .sorted(Comparator.comparing(PostsBranchReplyFacadeDto::createdAt))
                .collect(Collectors.groupingBy(PostsBranchReplyFacadeDto::rootReplyId));

        // 3. 루트 댓글 변환 및 자식 댓글 매칭
        List<PostsRootReplyFacadeDto> rootDtos = rootReplies.stream()
                .map(reply -> {
                    PostsRootReplyFacadeDto rootDto = toRootDto(reply, userIdToName);
                    List<PostsBranchReplyFacadeDto> children = groupedBranchReplies.getOrDefault(rootDto.getId(), List.of());
                    rootDto.setPostsChildReplyList(children);
                    return rootDto;
                })
                .toList();

        return new GetPostRepliesByIdFacadeResultDto(rootDtos);
    }

    private static PostsRootReplyFacadeDto toRootDto(
            final PostsReply reply,
            final Map<Long, String> userMap
    ) {
        boolean isRevised =
                !reply.getCreatedAt().truncatedTo(ChronoUnit.SECONDS)
                        .isEqual(reply.getLastModified()
                                .truncatedTo(ChronoUnit.SECONDS));

        return PostsRootReplyFacadeDto.builder()
                .id(reply.getId())
                .postId(reply.getPostId())
                .contents(reply.getContents())
                .registerId(reply.getRegisterId())
                .registerName(userMap.get(reply.getRegisterId()))
                .anonymous(BoolType.convertToBoolean(reply.getIsAnonymous()))
                .revised(isRevised)
                .deleted(BoolType.convertToBoolean(reply.getIsDeleted()))
                .likes(reply.getLikeCount() != null ? reply.getLikeCount() : 0)
                .isLiked(reply.getAmILiked() != null ? reply.getAmILiked() : false)
                .createdAt(reply.getCreatedAt())
                .build();
    }

    private static PostsBranchReplyFacadeDto toBranchDto(
            final PostsReply reply,
            final Map<Long, String> userMap
    ) {
        boolean isRevised =
                !reply.getCreatedAt().truncatedTo(ChronoUnit.SECONDS)
                        .isEqual(reply.getLastModified()
                                .truncatedTo(ChronoUnit.SECONDS));

        return PostsBranchReplyFacadeDto.builder()
                .id(reply.getId())
                .postId(reply.getPostId())
                .rootReplyId(reply.getRootReplyId())
                .contents(reply.getContents())
                .registerId(reply.getRegisterId())
                .registerName(userMap.get(reply.getRegisterId()))
                .anonymous(BoolType.convertToBoolean(reply.getIsAnonymous()))
                .revised(isRevised)
                .deleted(BoolType.convertToBoolean(reply.getIsDeleted()))
                .likes(reply.getLikeCount() != null ? reply.getLikeCount() : 0)
                .isLiked(reply.getAmILiked() != null ? reply.getAmILiked() : false)
                .createdAt(reply.getCreatedAt())
                .build();
    }
}