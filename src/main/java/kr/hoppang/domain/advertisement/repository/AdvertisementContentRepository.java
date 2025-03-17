package kr.hoppang.domain.advertisement.repository;

import java.util.List;
import kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto.GetAdvertisementContentListRepositoryDto;
import kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto.GetAdvertisementTrafficListRepositoryDto;
import kr.hoppang.domain.advertisement.AdvertisementContent;

public interface AdvertisementContentRepository {

    AdvertisementContent getAdvertisementContent(final String advId);

    void createAdvertisementContent(final AdvertisementContent advertisementContent);

    List<GetAdvertisementContentListRepositoryDto.Res> getAdvertisementContentList(
            final GetAdvertisementContentListRepositoryDto.Req repositoryReq
    );

    List<GetAdvertisementTrafficListRepositoryDto.Res> getAdvertisementTrafficList(
            final GetAdvertisementTrafficListRepositoryDto.Req repositoryReq);
}
