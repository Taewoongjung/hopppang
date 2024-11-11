package kr.hoppang.application.readmodel.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.EXPIRED_ACCESS_TOKEN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AUTHORIZED_USER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.adapter.outbound.cache.user.CacheUserRedisRepository;
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
    private final CacheUserRedisRepository cacheUserRedisRepository;

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
        String userEmail = jwtUtil.getEmail(token);

        log.info("email = {}", userEmail);

        User foundUser = null;

        // 캐시에서 먼저 찾는다.
        foundUser = cacheUserRedisRepository.getBucketByKey(userEmail);

        // 캐시에 유저 정보가 없으면 RDB 에서 찾는다.
        if (foundUser == null) {
            foundUser = userRepository.findByEmail(userEmail);

            // 일반 유저만 캐싱 한다. (관리자 유저는 캐싱 하지 않는다.)
            if (UserRole.ROLE_CUSTOMER.equals(foundUser.getUserRole())) {
                cacheUserRedisRepository.addUserInfoInCache(foundUser);
            }
        }

        log.info("foundUser = {}", foundUser.getUserTokenList());

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
