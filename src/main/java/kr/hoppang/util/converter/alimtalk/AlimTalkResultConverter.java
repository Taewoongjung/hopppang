package kr.hoppang.util.converter.alimtalk;

import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.alimtalk.AlimTalkResultEntity;
import kr.hoppang.domain.alimtalk.AlimTalkResult;

public class AlimTalkResultConverter {

    public static AlimTalkResultEntity toEntity(final AlimTalkResult alimTalkResult) {
        return AlimTalkResultEntity.builder()
                .id(alimTalkResult.getId())
                .templateName(alimTalkResult.getTemplateName())
                .templateCode(alimTalkResult.getTemplateCode())
                .messageSubject(alimTalkResult.getMessageSubject())
                .message(alimTalkResult.getMessage())
                .messageType(alimTalkResult.getMessageType())
                .receiverPhone(alimTalkResult.getReceiverPhone())
                .msgId(alimTalkResult.getMsgId())
                .isSuccess(alimTalkResult.getIsSuccess())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
