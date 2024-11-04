package kr.hoppang.adapter.outbound.cache.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import kr.hoppang.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@EnableRedisRepositories
@RequiredArgsConstructor
public class CacheUserRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void addUserInfoInCache(final String email, final User user) {
        saveData(email, user);
    }

    public User getBucketByKey(final String email) {
        return getData(email, User.class).orElse(null);
    }

    public <T> boolean saveData(String key, T data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch(Exception e){
            log.error(String.valueOf(e));
            return false;
        }
    }

    public <T> Optional<T> getData(String key, Class<T> classType) {
        String value = redisTemplate.opsForValue().get(key);

        if(value == null){
            return Optional.empty();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.of(objectMapper.readValue(value, classType));
        } catch(Exception e){
            log.error(String.valueOf(e));
            return Optional.empty();
        }
    }
}
