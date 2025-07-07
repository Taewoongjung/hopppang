package kr.hoppang.application.readmodel.chassis.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindAllEstimationsProvidedToCustomersQueryHandler implements IQueryHandler<EmptyQuery, Long> {

    private final ChassisEstimationRepository chassisEstimationRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Long handle(final EmptyQuery query) {

        return chassisEstimationRepository.findAllEstimationsProvidedToCustomer();
    }
}
