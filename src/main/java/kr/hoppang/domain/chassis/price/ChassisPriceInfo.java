package kr.hoppang.domain.chassis.price;

import java.time.LocalDateTime;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChassisPriceInfo {

    private CompanyType companyType;
    private ChassisType type;
    private Integer width;
    private Integer height;
    private Integer price;
    private LocalDateTime lastModified;


    private ChassisPriceInfo(
            final CompanyType companyType,
            final ChassisType type,
            final Integer width,
            final Integer height,
            final Integer price,
            final LocalDateTime lastModified
    ) {
        this.companyType = companyType;
        this.type = type;
        this.width = width;
        this.height = height;
        this.price = price;
        this.lastModified = lastModified;
    }

    public static ChassisPriceInfo of(
            final CompanyType companyType,
            final ChassisType type,
            final Integer width,
            final Integer height,
            final Integer price,
            final LocalDateTime lastModified
    ) {
        return new ChassisPriceInfo(companyType, type, width, height, price, lastModified);
    }
}
