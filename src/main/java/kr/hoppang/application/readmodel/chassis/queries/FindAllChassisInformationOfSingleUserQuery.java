package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;

public record FindAllChassisInformationOfSingleUserQuery(
        long userSrl
) implements IQuery {

}
