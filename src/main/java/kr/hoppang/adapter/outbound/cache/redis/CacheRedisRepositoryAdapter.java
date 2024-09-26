package kr.hoppang.adapter.outbound.cache.redis;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_DATA_IN_CACHE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import java.util.ArrayList;
import java.util.List;
import kr.hoppang.domain.cache.initializer.repository.CacheInitializeRepository;
import kr.hoppang.domain.chassis.price.ChassisPriceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

@Slf4j
@Primary
@Repository
@EnableRedisRepositories
@RequiredArgsConstructor
public class CacheRedisRepositoryAdapter implements CacheInitializeRepository<ChassisPriceInfo> {

    private final RedisTemplate<String, List<ChassisPriceInfo>> redisTemplate;

    @Override
    public Boolean setBucketOf(final String bucketKey, final List<ChassisPriceInfo> values) {

        ValueOperations<String, List<ChassisPriceInfo>> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(bucketKey, values);

        return true;
    }

    @Override
    public boolean refreshDataIn(final String bucketKey, final List<ChassisPriceInfo> values) {

        ValueOperations<String, List<ChassisPriceInfo>> valueOperations = redisTemplate.opsForValue();

        List<ChassisPriceInfo> refreshingValues = valueOperations.get(bucketKey);

        check(refreshingValues == null, NOT_EXIST_DATA_IN_CACHE);

        if (!values.toString().equals(refreshingValues.toString())) {
            valueOperations.set(bucketKey, values);
        }

        return true;
    }

    @Override
    public List<ChassisPriceInfo> getDataOf(final String bucketKey) {

        ValueOperations<String, List<ChassisPriceInfo>> valueOperations = redisTemplate.opsForValue();

        List<ChassisPriceInfo> refreshingValues = valueOperations.get(bucketKey);

        // 케시에 해당 버킷이 존재하지 않으면 생성
        if (refreshingValues == null) {
            valueOperations.set(bucketKey, new ArrayList<>());
        }

        return refreshingValues;
    }
}
