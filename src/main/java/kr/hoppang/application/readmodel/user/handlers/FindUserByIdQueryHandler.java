package kr.hoppang.application.readmodel.user.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindUserByIdQuery;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByIdQueryHandler implements IQueryHandler<FindUserByIdQuery, User> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    public User handle(final FindUserByIdQuery query) {

        return userRepository.findById(query.userId());
    }
}
