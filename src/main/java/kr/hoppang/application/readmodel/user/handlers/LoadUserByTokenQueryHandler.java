package kr.hoppang.application.readmodel.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.EXPIRED_ACCESS_TOKEN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AUTHORIZED_USER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.application.readmodel.user.queries.LoadUserByTokenQuery;
import kr.hoppang.config.security.jwt.JWTUtil;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
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

        // Bearer 제외 한 토큰 추출
        String token = jwtUtil.getTokenWithoutBearer(event.authToken());

        // 토큰에 해당하는 유저를 찾는다
        log.info("email = {}", jwtUtil.getEmail(token));
        User foundUser = userRepository.findByEmail(jwtUtil.getEmail(token));
        log.info("foundUser = {}", foundUser);

        // 토큰 만료 검사
        check(jwtUtil.isExpired(token), EXPIRED_ACCESS_TOKEN);

        log.info("{} 고객님 접속 중", foundUser.getName());

        // 어드민이 아닌 유저가 어드민 페이지에 접속하면 차단
        if (event.isAdminPage()) {
            check(foundUser.getUserRole() != UserRole.ROLE_ADMIN, NOT_AUTHORIZED_USER);
        }

        return foundUser;
    }
}
