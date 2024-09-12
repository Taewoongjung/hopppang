package kr.hoppang.application.readmodel.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.EXPIRED_ACCESS_TOKEN;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.application.readmodel.user.queries.LoadUserByTokenQuery;
import kr.hoppang.config.security.jwt.JWTUtil;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadUserByTokenQueryHandler implements IQueryHandler<LoadUserByTokenQuery, User> {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public User handle(final LoadUserByTokenQuery event) {
        log.info("LoadUserByTokenQuery {}", event.authToken());
        
        String token = jwtUtil.getTokenWithoutBearer(event.authToken());

        check(jwtUtil.isExpired(token), EXPIRED_ACCESS_TOKEN);

        return userRepository.findByEmail(jwtUtil.getEmail(token));
    }
}
