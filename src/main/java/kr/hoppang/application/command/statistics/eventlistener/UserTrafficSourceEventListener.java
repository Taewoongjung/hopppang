package kr.hoppang.application.command.statistics.eventlistener;

import kr.hoppang.application.command.statistics.event.UserTrafficSourceEvent;
import kr.hoppang.domain.advertisement.AdvertisementContent;
import kr.hoppang.domain.advertisement.repository.AdvertisementContentRepository;
import kr.hoppang.domain.statistics.repository.UserTrafficSourceRepository;
import kr.hoppang.domain.statistics.repository.dto.CreateUserTrafficSourceInfoRepositoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTrafficSourceEventListener {

    private final UserTrafficSourceRepository trafficSourceRepository;
    private final AdvertisementContentRepository advertisementContentRepository;


    @Async
    @Transactional
    @EventListener
    public void createUserTrafficSourceInfo(final UserTrafficSourceEvent event) {

        AdvertisementContent advertisementContent = null;
        try {
            advertisementContent = advertisementContentRepository.getAdvertisementContent(
                    event.advId());

        } catch (Exception err) {
            log.error("광고 문구를 찾기 못함 = {}", event.advId(), err);

            advertisementContent = advertisementContentRepository.getAdvertisementContent(
                    "contaminatedAdvId");
        }

        trafficSourceRepository.createUserTrafficSourceInfo(
                CreateUserTrafficSourceInfoRepositoryDto.builder()
                        .advertisementContentId(advertisementContent.getId())
                        .entryPageType(event.entryPageType())
                        .referrer(event.referrer())
                        .browser(event.browser())
                        .stayDuration(event.stayDuration())
                        .visitedAt(event.visitedAt())
                        .build()
        );
    }
}