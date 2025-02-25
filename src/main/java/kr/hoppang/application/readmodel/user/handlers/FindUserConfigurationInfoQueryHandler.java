package kr.hoppang.application.readmodel.user.handlers;

import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindUserConfigurationInfoQuery;
import kr.hoppang.domain.user.UserConfigInfo;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindUserConfigurationInfoQueryHandler implements IQueryHandler<FindUserConfigurationInfoQuery, UserConfigInfo> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public UserConfigInfo handle(final FindUserConfigurationInfoQuery query) {

        return userRepository.findUserConfigByUserId(query.userId());
    }
}
