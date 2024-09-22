package kr.hoppang.adapter.outbound.jpa.entity.chassis.price;

import java.io.Serializable;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ChassisPriceInfoId implements Serializable {
    private CompanyType companyType;
    private ChassisType type;
    private Integer width;
    private Integer height;

    public ChassisPriceInfoId(
            final CompanyType companyType,
            final ChassisType type,
            final Integer width,
            final Integer height
    ) {
        this.companyType = companyType;
        this.type = type;
        this.width = width;
        this.height = height;
    }
}
