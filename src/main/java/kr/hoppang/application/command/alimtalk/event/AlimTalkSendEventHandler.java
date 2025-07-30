package kr.hoppang.application.command.alimtalk.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlimTalkSendEventHandler {




    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle() {

    }
}
