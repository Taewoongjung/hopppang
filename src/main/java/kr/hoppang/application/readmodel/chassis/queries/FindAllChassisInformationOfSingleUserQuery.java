package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;
import org.springframework.data.domain.Pageable;

public record FindAllChassisInformationOfSingleUserQuery(
        long userSrl,
        Pageable pageable,
        long lastEstimationId
) implements IQuery {

}
