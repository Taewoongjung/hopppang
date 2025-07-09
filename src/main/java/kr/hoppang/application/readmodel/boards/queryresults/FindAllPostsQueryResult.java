package kr.hoppang.application.readmodel.boards.queryresults;

import java.util.List;
import kr.hoppang.domain.boards.Posts;
import lombok.Builder;

@Builder
public record FindAllPostsQueryResult(
        long count,
        List<Posts> postsList
) { }
