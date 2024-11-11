package kr.hoppang.application.readmodel.user.handlers;

import io.jsonwebtoken.lang.Assert;
import kr.hoppang.adapter.outbound.cache.user.CacheUserRedisRepository;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserRole;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadUserByUsernameQueryHandler implements UserDetailsService {

    private final UserRepository userRepository;
    private final CacheUserRedisRepository cacheUserRedisRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        log.info("LoadUserByUsernameQuery {}", email);

        User user = cacheUserRedisRepository.getBucketByKey(email);

        if (user == null) {
            return userRepository.findByEmailWithoutRelations(email);
        }

        return user;
    }
}
