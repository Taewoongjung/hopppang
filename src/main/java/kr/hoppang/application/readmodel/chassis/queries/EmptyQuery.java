package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;

@Builder
public record EmptyQuery() implements IQuery {

}
