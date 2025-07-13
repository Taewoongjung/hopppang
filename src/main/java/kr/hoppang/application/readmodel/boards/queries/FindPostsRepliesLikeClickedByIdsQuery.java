package kr.hoppang.application.readmodel.boards.queries;

import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

@Builder
public record FindPostsRepliesLikeClickedByIdsQuery(
        List<Long> replyIdList,
        Long loggedUserId
) implements IQuery { }