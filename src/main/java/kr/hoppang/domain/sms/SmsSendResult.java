package kr.hoppang.domain.sms;

import java.time.LocalDateTime;
import java.util.HashMap;
import kr.hoppang.util.common.BoolType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SmsSendResult {

    private final Long id;

    private final BoolType isSuccess;

    private final String type;

    private final String sender;

    private final String receiver;

    private final String platformType;

    private final String resultCode;

    private final String msgId;

    private final LocalDateTime createdAt;

    private SmsSendResult(
            final Long id,
            final BoolType isSuccess,
            final String type,
            final String sender,
            final String receiver,
            final String platformType,
            final String resultCode,
            final String msgId,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.isSuccess = isSuccess;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.platformType = platformType;
        this.resultCode = resultCode;
        this.msgId = msgId;
        this.createdAt = createdAt;
    }

    // 생성
    public static SmsSendResult of(
            final BoolType isSuccess,
            final String type,
            final String sender,
            final String receiver,
            final String platformType,
            final String resultCode,
            final String msgId
    ) {

        return new SmsSendResult(null, isSuccess, type, sender, receiver, platformType, resultCode,
                msgId, null);
    }

    public static SmsSendResult of(
            final String sender,
            final String receiver,
            final HashMap<String, String> json,
            final String platformType
    ) {

        SmsSendResult smsSendResult = null;

        try {
            if ("ALIGO".equals(platformType)) {
                if (json != null && "success".equals(json.get("message"))) {
                    smsSendResult = SmsSendResult.of(
                            BoolType.T,
                            json.get("msg_type"),
                            sender,
                            receiver,
                            platformType,
                            json.get("result_code"),
                            json.get("msg_id")
                    );
                } else if (json == null) {
                    smsSendResult = SmsSendResult.of(
                            BoolType.F,
                            "",
                            sender,
                            receiver,
                            platformType,
                            null,
                            ""
                    );
                } else {
                    smsSendResult = SmsSendResult.of(
                            BoolType.F,
                            "",
                            sender,
                            receiver,
                            platformType,
                            json.get("result_code"),
                            ""
                    );
                }
            }

        } catch (Exception e) {
            log.error("[Err_msg] SmsSendResult 객체 생성 중 json 파싱 실패 : {}", e.toString());
        }

        return smsSendResult;
    }

    // 조회
    public static SmsSendResult of(
            final Long id,
            final BoolType isSuccess,
            final String type,
            final String sender,
            final String receiver,
            final String platformType,
            final String resultCode,
            final String msgId,
            final LocalDateTime createdAt
    ) {

        return new SmsSendResult(id, isSuccess, type, sender, receiver, platformType, resultCode,
                msgId, createdAt);
    }
}
