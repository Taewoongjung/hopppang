package kr.hoppang.application.command.alimtalk.event.events;

import kr.hoppang.domain.alimtalk.AlimTalkTemplateType;
import lombok.Builder;

@Builder
public record SignUpWelcomeAlimTalkSendEvent(
        String receiverPhoneNumber,
        AlimTalkTemplateType templateCode
) { }
