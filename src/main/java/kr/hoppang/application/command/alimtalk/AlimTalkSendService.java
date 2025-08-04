package kr.hoppang.application.command.alimtalk;

import kr.hoppang.domain.alimtalk.AlimTalkTemplate;

public interface AlimTalkSendService {

    void send(
            String receiverPhoneNumber,
            AlimTalkTemplate alimTalkTemplate
    );
}
