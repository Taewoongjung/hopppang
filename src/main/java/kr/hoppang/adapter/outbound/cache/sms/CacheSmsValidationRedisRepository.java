package kr.hoppang.adapter.outbound.cache.sms;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import kr.hoppang.adapter.outbound.cache.dto.TearDownBucketByKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Repository
@EnableRedisRepositories
@RequiredArgsConstructor
public class CacheSmsValidationRedisRepository {

    private final RedisTemplate<String, Map<String, String>> redisTemplate;

    public void makeBucketByKey(final String key, final int number) {
        ValueOperations<String, Map<String, String>> stringStringValueOperations = redisTemplate.opsForValue();

        HashMap<String, String> map = new HashMap<>();
        map.put("number", String.valueOf(number));
        map.put("createdAt", LocalDateTime.now().toString());
        map.put("isVerified", "false");

        // 해당 버킷 ttl 을 3분 20초로 설정
        stringStringValueOperations.set(key, map, Duration.ofSeconds(200));
    }

    public Map<String, String> getBucketByKey(final String key) {

        ValueOperations<String, Map<String, String>> valueOps = redisTemplate.opsForValue();
        return valueOps.get(key);
    }

    public boolean getIsVerifiedByKey(final String key) {
        ValueOperations<String, Map<String, String>> valueOps = redisTemplate.opsForValue();

        Map<String, String> valueMap = valueOps.get(key);

        if (valueMap != null) {
            String value = valueMap.get("isVerified");

            if ("false".equals(value)) {
                return false;
            }
            return true;
        } else {
            log.error("[Redis] No value found for key: " + key);
        }

        return false;
    }

    public void setIsVerifiedByKey(final String key) {
        ValueOperations<String, Map<String, String>> valueOps = redisTemplate.opsForValue();

        Map<String, String> valueMap = valueOps.get(key);

        if (valueMap != null) {
            valueMap.put("isVerified", "true");

            // 기존 TTL 유지 (현재 TTL을 가져와서 다시 설정)
            Long currentTtl = redisTemplate.getExpire(key);

            valueOps.set(key, valueMap, Duration.ofSeconds(currentTtl));
        } else {
            log.error("[Redis] No value found for key: " + key);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tearDownBucketByKey(final TearDownBucketByKey req) {
        redisTemplate.delete(req.key());
    }
}
