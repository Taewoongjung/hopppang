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

    @Column(name = "chassis_estimation_info_id", nullable = false, columnDefinition = "bigint")
    private Long chassisEstimationInfoId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "chassis_type", nullable = false, columnDefinition = "varchar(20)")
    private ChassisType chassisType;

    private int width;

    private int height;

    private int price;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chassis_estimation_info_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ChassisEstimationInfoEntity chassisEstimationInfo;

    private ChassisEstimationSizeInfoEntity(
            final Long id,
            final Long chassisEstimationInfoId,
            final ChassisType chassisType,
            final int width,
            final int height,
            final int price
    ) {
        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.chassisEstimationInfoId = chassisEstimationInfoId;
        this.chassisType = chassisType;
        this.width = width;
        this.height = height;
        this.price = price;
    }

    // 생성
    public static ChassisEstimationSizeInfoEntity of(
            final Long chassisEstimationInfoId,
            final ChassisType chassisType,
            final int width,
            final int height,
            final int price
    ) {

        return new ChassisEstimationSizeInfoEntity(null, chassisEstimationInfoId, chassisType, width, height, price);
    }

    public void setChassisEstimationInfo(final ChassisEstimationInfoEntity chassisEstimationInfo) {
        this.chassisEstimationInfo = chassisEstimationInfo;
    }
}
