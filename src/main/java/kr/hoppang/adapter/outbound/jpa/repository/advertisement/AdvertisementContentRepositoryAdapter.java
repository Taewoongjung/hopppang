package kr.hoppang.adapter.outbound.jpa.repository.advertisement;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_ADVERTISEMENT_CONTENT;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.converter.advertisement.AdvertisementConverter.advertisementContentToEntity;

import kr.hoppang.adapter.outbound.jpa.entity.advertisement.AdvertisementContentEntity;
import kr.hoppang.domain.advertisement.AdvertisementContent;
import kr.hoppang.domain.advertisement.repository.AdvertisementContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdvertisementContentRepositoryAdapter implements AdvertisementContentRepository {

    private final AdvertisementContentJpaRepository advertisementContentJpaRepository;


    @Override
    public AdvertisementContent getAdvertisementContent(final String advId) {

        AdvertisementContentEntity advertisementContentEntity = advertisementContentJpaRepository.findByAdvId(
                advId);

        check(advertisementContentEntity == null, NOT_EXIST_ADVERTISEMENT_CONTENT);

        return advertisementContentEntity.toPojo();
    }

    @Override
    public void createAdvertisementContent(final AdvertisementContent advertisementContent) {
        advertisementContentJpaRepository.save(
                advertisementContentToEntity(advertisementContent)
        );
    }
}
