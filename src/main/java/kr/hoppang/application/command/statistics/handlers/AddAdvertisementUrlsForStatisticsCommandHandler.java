package kr.hoppang.application.command.statistics.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.statistics.commands.AddAdvertisementUrlsForStatisticsCommand;
import kr.hoppang.domain.advertisement.AdvertisementContent;
import kr.hoppang.domain.advertisement.repository.AdvertisementContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddAdvertisementUrlsForStatisticsCommandHandler implements
        ICommandHandler<AddAdvertisementUrlsForStatisticsCommand.Req, Void> {

    private final AdvertisementContentRepository advertisementContentRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Void handle(final AddAdvertisementUrlsForStatisticsCommand.Req command) {

        advertisementContentRepository.createAdvertisementContent(
                AdvertisementContent.of(
                        command.advId(),
                        command.targetPlatform(),
                        command.startedAt(),
                        null,
                        command.publisherId(),
                        command.memo()
                )
        );

        return null;
    }
}
