package kr.hoppang.application.readmodel.advertisement.handlers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto.GetAdvertisementContentListRepositoryDto;
import kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto.GetAdvertisementContentListRepositoryDto.Res;
import kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto.GetAdvertisementTrafficListRepositoryDto;
import kr.hoppang.application.readmodel.advertisement.queries.FindAdvertisementUrlsForStatisticsQuery;
import kr.hoppang.domain.advertisement.repository.AdvertisementContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindAdvertisementUrlsForStatisticsQueryHandler implements IQueryHandler<FindAdvertisementUrlsForStatisticsQuery.Req, List<FindAdvertisementUrlsForStatisticsQuery.Res>> {

    private final AdvertisementContentRepository advertisementContentRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FindAdvertisementUrlsForStatisticsQuery.Res> handle(
            final FindAdvertisementUrlsForStatisticsQuery.Req query) {

        List<GetAdvertisementContentListRepositoryDto.Res> adContentList = advertisementContentRepository.getAdvertisementContentList(
                GetAdvertisementContentListRepositoryDto.Req.builder()
                        .limit(query.limit())
                        .offset(query.offset())
                        .isOnAir(query.isOnAir())
                        .advChannel(query.advChannel())
                        .build()
        );

        if (adContentList.isEmpty()) {
            return Collections.emptyList();
        }

        List<GetAdvertisementTrafficListRepositoryDto.Res> adTrafficList = advertisementContentRepository.getAdvertisementTrafficList(
                GetAdvertisementTrafficListRepositoryDto.Req.builder()
                        .advertisementIdList(
                                Optional.of(adContentList)
                                        .map(e ->
                                                e.stream()
                                                        .map(Res::advertisementId)
                                                        .collect(Collectors.toSet())
                                        ).orElse(Collections.emptySet())
                        )
                        .advIdList(query.advIdList())
                        .isOnAir(query.isOnAir())
                        .advChannel(query.advChannel())
                        .build()
        );

        Map<String, Long> countOfEachTraffic = adTrafficList.stream()
                .collect(Collectors.groupingBy(
                                GetAdvertisementTrafficListRepositoryDto.Res::advId,
                                Collectors.counting()
                        )
                );

        return adContentList.stream()
                .map(adContent ->
                        FindAdvertisementUrlsForStatisticsQuery.Res.builder()
                                .advId(adContent.advId())
                                .advChannel(adContent.advChannel())
                                .startAt(adContent.startAt())
                                .endAt(adContent.endAt())
                                .publisherId(adContent.publisherId())
                                .publisherName(adContent.publisherName())
                                .memo(adContent.memo())
                                .clickCount(
                                        Optional.ofNullable(
                                                        countOfEachTraffic.get(adContent.advId())
                                                )
                                                .orElse(0L).intValue()
                                )
                                .build()
                ).toList();
    }
}
