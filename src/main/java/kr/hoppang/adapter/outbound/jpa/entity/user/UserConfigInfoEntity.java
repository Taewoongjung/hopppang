package kr.hoppang.adapter.outbound.jpa.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.user.UserConfigInfo;
import kr.hoppang.util.common.BoolType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "user_config_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserConfigInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_push_on", nullable = false, columnDefinition = "char(1)")
    private BoolType isPushOn;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "is_alim_talk_on", nullable = false, columnDefinition = "char(1)")
    private BoolType isAlimTalkOn;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;


    private UserConfigInfoEntity(
            final Long id,
            final Long userId,
            final BoolType isPushOn,
            final BoolType isAlimTalkOn
    ) {
        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.userId = userId;
        this.isPushOn = isPushOn;
        this.isAlimTalkOn = isAlimTalkOn;
    }

    public static UserConfigInfoEntity of(
            final Long userId,
            final BoolType isPushOn,
            final BoolType isAlimTalkOn
    ) {
        return new UserConfigInfoEntity(null, userId, isPushOn, isAlimTalkOn);
    }

    public UserConfigInfo toPojo() {
        return UserConfigInfo.of(
                id,
                userId,
                isPushOn,
                isAlimTalkOn,
                getCreatedAt(),
                getLastModified());
    }

    public void setIsPushOn(final BoolType isPushOn) {
        this.isPushOn = isPushOn;
        updateLastModifiedAsNow();
    }
}
