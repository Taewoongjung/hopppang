package kr.hoppang.application.util;

import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

@Builder
public record EmptyQuery() implements IQuery {

    public static EmptyQuery of() {
        return new EmptyQuery();
    }
}
