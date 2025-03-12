package kr.hoppang.domain.advertisement.repository;

import kr.hoppang.domain.advertisement.AdvertisementContent;

public interface AdvertisementContentRepository {

    AdvertisementContent getAdvertisementContent(final String advId);
}
