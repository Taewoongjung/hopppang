package kr.hoppang.application.readmodel.user.handlers;

import java.util.List;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.user.queries.GetStatisticsOfUserQuery;
import kr.hoppang.application.readmodel.user.queries.GetStatisticsOfUserQuery.Request;
import kr.hoppang.application.readmodel.user.queries.GetStatisticsOfUserQuery.Response;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetStatisticsOfUserQueryHandler implements IQueryHandler<GetStatisticsOfUserQuery.Request, GetStatisticsOfUserQuery.Response> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Response handle(final Request query) {

        List<User> allRegisteredUsersWithOutAnyFiltered = userRepository.findAllUsersBetween(query.getStartTimeForSearch(),
                query.getEndTimeForSearch());

        return Response.of(null);
    }
}
