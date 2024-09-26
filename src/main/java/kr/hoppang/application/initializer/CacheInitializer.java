package kr.hoppang.application.initializer;

import static kr.hoppang.domain.chassis.price.ChassisPriceInfo.mapToChassisPriceInfo;
import static kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria.mapToAdditionalChassisPriceCriteria;

import jakarta.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import kr.hoppang.domain.cache.CacheData;
import kr.hoppang.domain.cache.initializer.repository.CacheInitializeRepository;
import kr.hoppang.domain.chassis.price.ChassisPriceInfo;
import kr.hoppang.domain.chassis.price.pricecriteria.AdditionalChassisPriceCriteria;
import kr.hoppang.domain.chassis.price.repository.ChassisPriceInfoRepository;
import kr.hoppang.domain.chassis.price.repository.pricecriteria.AdditionalChassisPriceCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheInitializer {

    private final CacheInitializeRepository cacheInitializeRepository;
    private final ChassisPriceInfoRepository chassisPriceInfoRepository;
    private final AdditionalChassisPriceCriteriaRepository additionalChassisPriceCriteriaRepository;

    private static final String BUCKET_CHASSIS_PRICE_INFO = "chassisPriceInfo";
    private static final String BUCKET_ADDITIONAL_CHASSIS_PRICE_CRITERIA = "additionalChassisPriceCriteria";

    @PostConstruct
    public void initCache() {
        updateCacheIfNeeded(BUCKET_CHASSIS_PRICE_INFO,
                chassisPriceInfoRepository::findAll,
                this::convertMapToChassisPriceInfo,
                this::convertListToString);

        updateCacheIfNeeded(BUCKET_ADDITIONAL_CHASSIS_PRICE_CRITERIA,
                additionalChassisPriceCriteriaRepository::findAll,
                this::convertMapToAdditionalChassisPriceCriteria,
                this::convertListToString);
    }

    private <T extends CacheData> void updateCacheIfNeeded(
            final String bucketName,
            final Supplier<List<T>> dbFetcher,
            final Function<LinkedHashMap<String, Object>, T> mapper,
            final Function<List<T>, String> stringConverter
    ) {
        List<T> dbData = dbFetcher.get();
        List<Object> cacheData = cacheInitializeRepository.getDataOf(bucketName);

        List<T> mappedCacheData = cacheData.stream()
                .map(e -> mapper.apply((LinkedHashMap<String, Object>) e))
                .toList();

        if (!stringConverter.apply(mappedCacheData).equals(stringConverter.apply(dbData))) {
            cacheInitializeRepository.setBucketOf(bucketName, dbData);
        }
    }

    private ChassisPriceInfo convertMapToChassisPriceInfo(final LinkedHashMap<String, Object> map) {
        return mapToChassisPriceInfo(map);
    }

    private AdditionalChassisPriceCriteria convertMapToAdditionalChassisPriceCriteria(final LinkedHashMap<String, Object> map) {
        return mapToAdditionalChassisPriceCriteria(map);
    }

    private <T extends CacheData> String convertListToString(final List<T> list) {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
