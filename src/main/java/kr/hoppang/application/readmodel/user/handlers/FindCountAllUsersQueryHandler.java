package kr.hoppang.application.readmodel.user.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.chassis.queries.EmptyQuery;
import kr.hoppang.application.readmodel.user.queries.FindCountAllUsersQuery;
import kr.hoppang.application.readmodel.user.queries.FindCountAllUsersQuery.Res;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindCountAllUsersQueryHandler implements IQueryHandler<EmptyQuery, FindCountAllUsersQuery.Res> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Res handle(final EmptyQuery query) {

        return Res.builder()
                .count(userRepository.findCountOfAllUsers())
                .build();
    }
}
