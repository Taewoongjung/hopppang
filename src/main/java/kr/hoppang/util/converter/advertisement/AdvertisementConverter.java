package kr.hoppang.util.converter.advertisement;

import kr.hoppang.adapter.outbound.jpa.entity.advertisement.AdvertisementContentEntity;
import kr.hoppang.domain.advertisement.AdvertisementContent;

public class AdvertisementConverter {

    public static AdvertisementContentEntity advertisementContentToEntity(
            final AdvertisementContent pojo) {

        return AdvertisementContentEntity.of(
                pojo.getAdvId(),
                pojo.getAdvChannel(),
                pojo.getStartAt(),
                pojo.getEndAt(),
                pojo.getPublisherId(),
                pojo.getMemo()
        );
    }
}
