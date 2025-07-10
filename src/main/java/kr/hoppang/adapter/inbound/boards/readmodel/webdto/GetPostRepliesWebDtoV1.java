package kr.hoppang.adapter.inbound.boards.readmodel.webdto;

import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.inbound.boards.readmodel.facade.dto.GetPostRepliesByIdFacadeResultDto;
import lombok.AccessLevel;
import lombok.Builder;

public record GetPostRepliesWebDtoV1() {

    public record Res(
            List<PostReplyWebDto> postReplyList
    ) {

        @Builder(access = AccessLevel.PRIVATE)
        private record PostReplyWebDto(
                Long id,
                Long postId,
                Long rootReplyId,
                String contents,
                Long registerId,
                String registerName,
                boolean isAnonymous,
                boolean hasRevised,
                LocalDateTime createdAt
        ) { }

        public static Res of(final GetPostRepliesByIdFacadeResultDto facadeResultDto) {
            return new Res(
                    facadeResultDto.postsReplyList().stream()
                            .map(facadeResult ->
                                    PostReplyWebDto.builder()
                                            .id(facadeResult.id())
                                            .postId(facadeResult.postId())
                                            .rootReplyId(facadeResult.rootReplyId())
                                            .contents(facadeResult.contents())
                                            .registerId(facadeResult.registerId())
                                            .registerName(facadeResult.registerName())
                                            .isAnonymous(facadeResult.isAnonymous())
                                            .hasRevised(facadeResult.hasRevised())
                                            .createdAt(facadeResult.createdAt())
                                            .build()
                            ).toList()
            );
        }
    }
}
