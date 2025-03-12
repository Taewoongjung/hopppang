package kr.hoppang.adapter.outbound.jpa.repository.advertisement;

import kr.hoppang.adapter.outbound.jpa.entity.advertisement.AdvertisementContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementContentJpaRepository extends
        JpaRepository<AdvertisementContentEntity, Long> {

    AdvertisementContentEntity findByAdvId(final String advId);
}
