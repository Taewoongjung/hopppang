package kr.hoppang.application.readmodel.user.queries;

import java.util.List;
import kr.hoppang.abstraction.domain.IQuery;
import kr.hoppang.domain.user.User;
import lombok.Builder;

public record GetAllUsersQuery() {

    @Builder
    public record Request(
            long offset,
            int limit
    ) implements IQuery { }

    public record Response(
            List<User> userList,
            long count
    ) { }
}
