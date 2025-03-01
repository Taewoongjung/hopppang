package kr.hoppang.adapter.outbound.cache.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
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

    public void addUserInfoInCache(final User user) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String key = "users::" + user.getEmail();
            String userJson = objectMapper.writeValueAsString(user);

            redisTemplate.opsForValue().set(key, userJson, Duration.ofHours(1));

        } catch (JsonProcessingException e) {
            // 예외 처리
            log.error("Exception [Err_location] : {}", e.getStackTrace()[0]);
            log.error("Exception [Err_msg] : {}", e.toString());
        }
    }

    public User getBucketByKey(final String email) {
        String key = "users::" + email;
        String userJson = redisTemplate.opsForValue().get(key);
        if (userJson != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                return objectMapper.readValue(userJson, User.class);
            } catch (JsonProcessingException e) {
                // 예외 처리
                log.error("Exception [Err_location] : {}", e.getStackTrace()[0]);
                log.error("Exception [Err_msg] : {}", e.toString());
            }
        }
        return null;
    }

    public <T> boolean saveData(String key, T data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
