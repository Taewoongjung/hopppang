package kr.hoppang.adapter.outbound.jpa.entity.alimtalk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.alimtalk.AlimTalkButtonLinkType;
import kr.hoppang.domain.alimtalk.AlimTalkTemplate;
import kr.hoppang.domain.alimtalk.AlimTalkThirdPartyType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "alim_talk_template")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlimTalkTemplateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "third_party_type", nullable = false, columnDefinition = "varchar(20)")
    private AlimTalkThirdPartyType thirdPartyType;

    private String templateName;

    private String templateCode;

    private String messageSubject;

    private String message;

    private String messageExtra;

    private String replaceMessageSubject;

    private String replaceMessage;

    private String buttonInfo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "button_link_type", nullable = false, columnDefinition = "char(2)")
    private AlimTalkButtonLinkType buttonLinkType;


    @Builder
    public AlimTalkTemplateEntity(
            Long id,
            AlimTalkThirdPartyType thirdPartyType,
            String templateName,
            String templateCode,
            String messageSubject,
            String message,
            String messageExtra,
            String replaceMessageSubject,
            String replaceMessage,
            String buttonInfo,
            AlimTalkButtonLinkType buttonLinkType
    ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.thirdPartyType = thirdPartyType;
        this.templateName = templateName;
        this.templateCode = templateCode;
        this.messageSubject = messageSubject;
        this.message = message;
        this.messageExtra = messageExtra;
        this.replaceMessageSubject = replaceMessageSubject;
        this.replaceMessage = replaceMessage;
        this.buttonInfo = buttonInfo;
        this.buttonLinkType = buttonLinkType;
    }

    public AlimTalkTemplate toPojo() {
        return AlimTalkTemplate.builder()
                .id(this.id)
                .thirdPartyType(this.thirdPartyType)
                .templateName(this.templateName)
                .templateCode(this.templateCode)
                .messageSubject(this.messageSubject)
                .message(this.message)
                .messageExtra(this.messageExtra)
                .replaceMessageSubject(this.replaceMessageSubject)
                .replaceMessage(this.replaceMessage)
                .buttonInfo(this.buttonInfo)
                .buttonLinkType(this.buttonLinkType)
                .build();
    }
}
