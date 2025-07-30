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
import kr.hoppang.domain.alimtalk.AlimTalkMessageType;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Table(name = "alim_talk_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlimTalkResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String templateName;

    private String templateCode;

    private String messageSubject;

    private String message;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "message_type", nullable = false, columnDefinition = "char(3)")
    private AlimTalkMessageType messageType;

    private String receiverPhone;

    private String msgId;

    @Column(name = "is_success", columnDefinition = "char(1)")
    @Enumerated(value = EnumType.STRING)
    private BoolType isSuccess;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;


    @Builder
    public AlimTalkResultEntity(
            Long id,
            String templateName,
            String templateCode,
            String messageSubject,
            String message,
            AlimTalkMessageType messageType,
            String receiverPhone,
            String msgId,
            BoolType isSuccess,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.templateName = templateName;
        this.templateCode = templateCode;
        this.messageSubject = messageSubject;
        this.message = message;
        this.messageType = messageType;
        this.receiverPhone = receiverPhone;
        this.msgId = msgId;
        this.isSuccess = isSuccess;
        this.createdAt = createdAt;
    }
}
