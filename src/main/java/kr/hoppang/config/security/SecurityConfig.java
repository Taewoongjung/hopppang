package kr.hoppang.config.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import kr.hoppang.application.readmodel.user.handlers.LoadUserByUsernameQueryHandler;
import kr.hoppang.config.security.jwt.JWTFilter;
import kr.hoppang.config.security.jwt.JWTUtil;
import kr.hoppang.config.security.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoadUserByUsernameQueryHandler loadUserByUsernameQueryHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Value("${server.front.admin-page-origin-url}")
    private String adminPageOriginUrl;

    @Value("${server.front.web-page-origin-url}")
    private String webPageOriginUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((corsCustomizer -> corsCustomizer.configurationSource(
                        new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(
                                    @NotNull HttpServletRequest request) {

                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Arrays
                                        .asList(adminPageOriginUrl, webPageOriginUrl));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                return configuration;
                            }
                        })));

        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((auth) -> auth

                        // all permitted
                        .requestMatchers(HttpMethod.GET,
                                "/actuator/info"
                                , "/actuator/health"
                                , "/actuator/prometheus"
                                , "/api/emails/validations"
                                , "/api/phones/validations"
                                , "/api/users/emails"
                                , "/api/v1/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/login"
                                , "/api/signup"
                                , "/api/kakao/auth"
                                , "/api/kakao/signup/{code}"
                                , "/api/chassis/calculations/prices"
                        ).permitAll()
                        .requestMatchers(HttpMethod.PUT,
                                "/api/kakao/refresh"
                        ).permitAll()

                        // authorized
                        .requestMatchers(HttpMethod.GET,
                                "/api/me"
                        ).authenticated()

                        .requestMatchers(HttpMethod.GET,
                                "/api/chassis/prices"
                                , "/api/chassis/prices/additions/criteria"
                                , "/api/chassis/estimations"
                                , "/api/chassis/estimations/count"
                        ).authenticated()

                        .requestMatchers(HttpMethod.POST
                                , "/api/chassis/prices"
//                                , "/api/chassis/calculations/prices"
                        ).authenticated()

                        .requestMatchers(HttpMethod.PUT
                                , "/api/chassis/prices/additions/criteria"
                        ).authenticated()

                        .requestMatchers("/admin").hasRole("CUSTOMER")
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTFilter(jwtUtil, loadUserByUsernameQueryHandler),
                        LoginFilter.class)

                // 로그인 필터 앞에서 JWTFilter 검증
                .addFilterAt(
                        new LoginFilter(authenticationManager(authenticationConfiguration),
                                jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 비밀번호 평문 저장을 방지하기 위한 인코더 빈등록
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
