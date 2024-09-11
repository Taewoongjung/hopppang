package kr.hoppang.config.security.jwt;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_AUTHORIZED_USER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.domain.user.UserRole.ROLE_ADMIN;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.hoppang.application.readmodel.user.handlers.LoadUserByUsernameQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final LoadUserByUsernameQueryHandler loadUserByUsernameQueryHandler;

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static final String[] EXCLUDED_PATH = {
            "/api/chassis/calculations/prices"
//      예시)
//
//            "/api/emails/validations"
//            , "/api/phones/validations"
//            , "/api/users/emails"
//            , "/api/users/verify"
//            , "/api/users/passwords"
    };

    private static final String[] API_ONLY_FOR_ADMIN = {
            "/api/chassis/prices"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        for (String api : EXCLUDED_PATH) {
            if (antPathMatcher.match(api, request.getServletPath())) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // request 에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.warn("token null");
            filterChain.doFilter(request, response);

            // 이 조건에 해당되면 메소드 종료
            return;
        }

        String token = jwtUtil.getTokenWithoutBearer(authorization);

        // 토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException jwtException) {
            log.info("토큰 만료: {}", token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized status code
            return;
        }

        // 토큰에서 email, role 값 획득
        String email = jwtUtil.getEmail(token);

        // 토큰으로 User 객체 생성
        UserDetails user = loadUserByUsernameQueryHandler.loadUserByUsername(email);

        // 어드민 전용 API 검증
        for (String api : API_ONLY_FOR_ADMIN) {
            if (antPathMatcher.match(api, request.getServletPath())) {
                check(user.getAuthorities().stream().noneMatch(e-> e.getAuthority().equals(ROLE_ADMIN.name())), NOT_AUTHORIZED_USER);
            }
        }

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(user, null,
                user.getAuthorities());

        // 세선에 사용자 등록 (유저 세선 등록)
        SecurityContextHolder.getContext().setAuthentication(authToken);

        response.setHeader("Authorization", token);

        filterChain.doFilter(request, response);
    }
}
