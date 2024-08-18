package kr.hoppang.application.readmodel.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.EXPIRED_ACCESS_TOKEN;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

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
public class LoadUserByTokenQueryHandler implements IQueryHandler<String, User> {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public User handle(final String reqToken) {
        log.info("LoadUserByTokenQuery {}", reqToken);
        
        String token = jwtUtil.getTokenWithoutBearer(reqToken);

        check(jwtUtil.isExpired(token), EXPIRED_ACCESS_TOKEN);

        User user = userRepository.findByEmail(jwtUtil.getEmail(token));

        if (user != null) {
            return user;
        }

        return null;
    }
}
