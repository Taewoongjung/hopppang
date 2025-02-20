package kr.hoppang.adapter.outbound.jpa.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.domain.user.UserLoginHistory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@ToString
@Table(name = "user_login_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    private UserLoginHistoryEntity(
            final Long id,
            final Long userId,
            final LocalDateTime createdAt
    ) {

        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public static UserLoginHistoryEntity of(
            final Long userId,
            final LocalDateTime createdAt
    ) {
        return new UserLoginHistoryEntity(null, userId, createdAt);
    }

    public UserLoginHistory toPojo() {
        return UserLoginHistory.of(
                getId(),
                getUserId(),
                getCreatedAt()
        );
    }
}
