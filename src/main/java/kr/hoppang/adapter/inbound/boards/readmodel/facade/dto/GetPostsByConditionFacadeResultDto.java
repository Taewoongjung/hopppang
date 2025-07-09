package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.user.User;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Builder;

@Builder
public record GetPostsByConditionFacadeResultDto(
        long count,
        List<PostsDto> postsList
) {

    @Builder(access = AccessLevel.PRIVATE)
    public record PostsDto(
            Long id,
            Long boardId,
            String authorName,
            String title,
            String contents,
            Boolean isAnonymous,
            Boolean isRevised,
            LocalDateTime createdAt
    ) { }

    public static GetPostsByConditionFacadeResultDto of(
            long count,
            List<Posts> postsList,
            List<User> authorList
    ) {

        return GetPostsByConditionFacadeResultDto.builder()
                .count(count)
                .postsList(
                        postsList.stream()
                                .map(post -> {
                                    User author = authorList.stream()
                                            .filter(f -> post.getRegisterId().equals(f.getId()))
                                            .findFirst()
                                            .orElse(null);

                                    return PostsDto.builder()
                                            .id(post.getId())
                                            .boardId(post.getBoardId())
                                            .authorName(
                                                    getAuthorName(author, post.getIsAnonymous())
                                            )
                                            .title(post.getTitle())
                                            .contents(post.getContents())
                                            .isRevised(
                                                    post.getCreatedAt()
                                                            .isEqual(post.getLastModified())
                                            )
                                            .createdAt(post.getCreatedAt())
                                            .build();
                                })
                                .toList()
                )
                .build();
    }

    private static String getAuthorName(final User author, final BoolType isAnonymous) {
        if (author == null) {
            return "알수없음";
        }

        if (author.isDeleted()) {
            return "탈퇴유저";
        }

        if (BoolType.T.equals(isAnonymous)) {
            return "익명";
        }

        return author.getName();
    }
}
