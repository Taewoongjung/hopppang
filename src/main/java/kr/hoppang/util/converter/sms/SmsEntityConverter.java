package kr.hoppang.util.converter.sms;

import kr.hoppang.adapter.outbound.jpa.entity.sms.SmsSendResultEntity;
import kr.hoppang.domain.sms.SmsSendResult;

public class SmsEntityConverter {

    public static SmsSendResultEntity smsSendResultToEntity(final SmsSendResult smsSendResult) {
        return SmsSendResultEntity.of(
                smsSendResult.getIsSuccess(),
                smsSendResult.getType(),
                smsSendResult.getSender(),
                smsSendResult.getReceiver(),
                smsSendResult.getPlatformType(),
                smsSendResult.getResultCode(),
                smsSendResult.getMsgId()
        );
    }

}
