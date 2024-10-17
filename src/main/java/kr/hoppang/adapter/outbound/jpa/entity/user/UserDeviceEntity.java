package kr.hoppang.adapter.outbound.jpa.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.domain.user.UserDevice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@ToString
@Table(name = "user_device_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(name = "device_type", columnDefinition = "char(10)")
    private String deviceType;

    @Column(name = "device_id", nullable = false, columnDefinition = "varchar(100)")
    private String deviceId;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    private UserDeviceEntity(
            final Long id,
            final Long userId,
            final String deviceType,
            final String deviceId
    ) {
        this.id = id;
        this.userId = userId;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.createdAt = LocalDateTime.now();
    }

    public static UserDeviceEntity of(
            final Long userId,
            final String deviceType,
            final String deviceId
    ) {
        return new UserDeviceEntity(null, userId, deviceType, deviceId);
    }

    public UserDevice toPojo() {
        return UserDevice.of(
                getId(),
                getUserId(),
                getDeviceType(),
                getDeviceId(),
                getCreatedAt()
        );
    }
}
