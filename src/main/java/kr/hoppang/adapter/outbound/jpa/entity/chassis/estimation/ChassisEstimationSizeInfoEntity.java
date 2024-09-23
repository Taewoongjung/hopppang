package kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.chassis.ChassisType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "chassis_estimation_size_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChassisEstimationSizeInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "chassis_type", nullable = false, columnDefinition = "varchar(20)")
    private ChassisType chassisType;

    private int width;

    private int height;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chassis_estimation_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ChassisEstimationInfoEntity chassisEstimationInfo;

    private ChassisEstimationSizeInfoEntity(
            final Long id,
            final ChassisType chassisType,
            final int width,
            final int height,
            final ChassisEstimationInfoEntity chassisEstimationInfo
    ) {
        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.chassisType = chassisType;
        this.width = width;
        this.height = height;
        this.chassisEstimationInfo = chassisEstimationInfo;
    }

    // 생성
    public static ChassisEstimationSizeInfoEntity of(
            final ChassisType chassisType,
            final int width,
            final int height,
            final ChassisEstimationInfoEntity chassisEstimationInfo
    ) {

        return new ChassisEstimationSizeInfoEntity(null, chassisType, width, height, chassisEstimationInfo);
    }

}
