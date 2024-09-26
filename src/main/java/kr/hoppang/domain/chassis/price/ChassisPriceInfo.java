package kr.hoppang.domain.chassis.price;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import kr.hoppang.domain.cache.CacheData;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import lombok.Getter;

@Getter
public class ChassisPriceInfo implements CacheData {

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

    public static ChassisPriceInfo mapToChassisPriceInfo(
            final LinkedHashMap<String, Object> map) {

        // LocalDateTime 변환
        String lastModifiedStr = (String) map.get("lastModified");
        LocalDateTime lastModified = LocalDateTime.parse(lastModifiedStr, DateTimeFormatter.ISO_DATE_TIME);

        return ChassisPriceInfo.of(
                (CompanyType.valueOf((String) map.get("companyType"))),
                (ChassisType.valueOf((String) map.get("type"))),
                ((Integer) map.get("width")),
                ((Integer) map.get("height")),
                ((Integer) map.get("price")),
                lastModified
        );
    }

    @Override
    public String toString() {
        return "ChassisPriceInfo{" +
                "companyType=" + companyType.name() +
                ", type=" + type.name() +
                ", width=" + width +
                ", height=" + height +
                ", price=" + price +
                ", lastModified=" + lastModified +
                '}';
    }
}
