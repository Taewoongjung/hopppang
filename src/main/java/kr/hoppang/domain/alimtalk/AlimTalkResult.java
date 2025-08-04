package kr.hoppang.domain.alimtalk;

import java.time.LocalDateTime;
import kr.hoppang.util.common.BoolType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlimTalkResult {

    private Long id;
    private String templateName;
    private String templateCode;
    private String messageSubject;
    private String message;
    private AlimTalkMessageType messageType;
    private String receiverPhone;
    private String msgId;
    private BoolType isSuccess;
    private LocalDateTime createdAt;
}
