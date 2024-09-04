package kr.hoppang.adapter.outbound.jpa.entity.chassis;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.ChassisPriceInfo;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@ToString
@IdClass(ChassisPriceInfoId.class)
@Table(name = "chassis_price_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChassisPriceInfoEntity {

    @Id
    @Enumerated(value = EnumType.STRING)
    @Column(name = "company_type", nullable = false, columnDefinition = "varchar(20)")
    private CompanyType companyType;

    @Id
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20)")
    private ChassisType type;

    @Id
    @Column(name = "width", nullable = false, columnDefinition = "int")
    private Integer width;

    @Id
    @Column(name = "height", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '샤시의 높이'")
    private Integer height;

    @Column(name = "price", nullable = false, columnDefinition = "INT UNSIGNED COMMENT '해당 샤시의 종류, 너비, 높이에 따른 가격'")
    private Integer price;

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL ON UPDATE CURRENT_TIMESTAMP(6)")
    private LocalDateTime lastModified;

    private ChassisPriceInfoEntity(
            final CompanyType companyType,
            final ChassisType type,
            final Integer width,
            final Integer height,
            final Integer price
    ) {
        this.companyType = companyType;
        this.type = type;
        this.width = width;
        this.height = height;
        this.price = price;
        this.lastModified = LocalDateTime.now();
    }

    // 생성
    public static ChassisPriceInfoEntity of(
            final CompanyType companyType,
            final ChassisType type,
            final Integer width,
            final Integer height,
            final Integer price
    ) {
        return new ChassisPriceInfoEntity(companyType, type, width, height, price);
    }

    public ChassisPriceInfo toPojo() {
        return ChassisPriceInfo.of(
            this.companyType,
            this.type,
            this.width,
            this.height,
            this.price,
            this.lastModified
        );
    }
}
