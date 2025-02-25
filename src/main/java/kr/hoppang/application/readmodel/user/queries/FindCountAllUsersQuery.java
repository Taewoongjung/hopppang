package kr.hoppang.application.readmodel.user.queries;

import lombok.Builder;

public record FindCountAllUsersQuery() {

    @Builder
    public record Res(
            long count
    ) { }
}
