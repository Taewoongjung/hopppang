package kr.hoppang.application.readmodel.chassis.queries;

import kr.hoppang.abstraction.domain.IQuery;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record FindAllChassisInformationOfSingleUserQuery(
        long userId,
        Pageable pageable,
        long lastEstimationId
) implements IQuery {

}
