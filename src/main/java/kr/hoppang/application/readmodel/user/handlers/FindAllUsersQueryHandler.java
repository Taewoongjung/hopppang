package kr.hoppang.application.readmodel.user.handlers;

import java.util.List;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindAllUsersQuery;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import kr.hoppang.util.CountQueryExecutionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindAllUsersQueryHandler implements IQueryHandler<FindAllUsersQuery.Request, FindAllUsersQuery.Response> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public FindAllUsersQuery.Response handle(final FindAllUsersQuery.Request query) {

        List<User> userList = userRepository.findAllAvailableUsers(query.offset(), query.limit());

        long count = CountQueryExecutionUtil.count(
                userList,
                query.offset(),
                query.limit(),
                userRepository::findCountOfAllAvailableUsers
        );

        return new FindAllUsersQuery.Response(userList, count);
    }
}
