package kr.hoppang.application.readmodel.user.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindCountAllUsersQueryHandler implements IQueryHandler<EmptyQuery, Long> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Long handle(final EmptyQuery query) {

        return userRepository.findCountOfAllUsers();
    }
}
