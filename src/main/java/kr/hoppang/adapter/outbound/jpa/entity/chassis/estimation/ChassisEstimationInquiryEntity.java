package kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInquiry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@ToString
@Table(name = "chassis_estimation_inquiry")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChassisEstimationInquiryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "chassis_estimation_info_id", nullable = false, columnDefinition = "bigint")
    private Long chassisEstimationInfoId;

    private String strategy;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    private ChassisEstimationInquiryEntity(
            final Long id,
            final Long userId,
            final Long chassisEstimationInfoId,
            final String strategy,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.chassisEstimationInfoId = chassisEstimationInfoId;
        this.strategy = strategy;
        this.createdAt = createdAt;
    }

    public static ChassisEstimationInquiryEntity of(
            final Long userId,
            final Long chassisEstimationInfoId,
            final String strategy
    ) {
        return new ChassisEstimationInquiryEntity(
                null,
                userId,
                chassisEstimationInfoId,
                strategy,
                LocalDateTime.now()
        );
    }

    public ChassisEstimationInquiry toPojo() {
        return ChassisEstimationInquiry.of(
                getId(),
                getUserId(),
                getChassisEstimationInfoId(),
                getStrategy(),
                getCreatedAt()
        );
    }
}
