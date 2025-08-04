package kr.hoppang.application.command.alimtalk.event.handlers;

import kr.hoppang.application.command.alimtalk.AlimTalkSendService;
import kr.hoppang.application.command.alimtalk.event.events.SignUpWelcomeAlimTalkSendEvent;
import kr.hoppang.domain.alimtalk.AlimTalkTemplate;
import kr.hoppang.domain.alimtalk.repository.AlimTalkTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SignUpWelcomeAlimTalkSendEventHandler {

    private final AlimTalkSendService alimTalkSendService;
    private final AlimTalkTemplateRepository alimTalkTemplateRepository;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(final SignUpWelcomeAlimTalkSendEvent event) {
        AlimTalkTemplate alimTalkTemplate = alimTalkTemplateRepository.findByTemplateType(
                event.templateCode());

        if (alimTalkTemplate != null) {
            alimTalkSendService.send(
                    event.receiverPhoneNumber(),
                    alimTalkTemplate
            );
        }
    }
}
