package kr.hoppang.domain.alimtalk;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlimTalkTemplate {

    private Long id;
    private AlimTalkThirdPartyType thirdPartyType;
    private String templateName;
    private String templateCode;
    private String messageSubject;
    private String message;
    private String messageExtra;
    private String replaceMessageSubject;
    private String replaceMessage;
    private String buttonInfo;
    private AlimTalkButtonLinkType buttonLinkType;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
}
