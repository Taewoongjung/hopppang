package kr.hoppang.adapter.inbound.boards.readmodel.facade.dto;

import java.util.List;
import kr.hoppang.domain.boards.Posts;
import lombok.Builder;

@Builder
public record GetPostsByConditionFacadeResultDto(
        long count,
        List<Posts> postsList
) { }
