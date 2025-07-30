package kr.hoppang.application.readmodel.user.queries;

import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

@Builder
public record FindUserByIdQuery(
        long userId,
        Boolean withDeletedUser
) implements IQuery { }
