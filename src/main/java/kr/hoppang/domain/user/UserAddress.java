package kr.hoppang.domain.user;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserAddress {

    private Long id;
    private Long userId;
    private String address;
    private String subAddress;
    private String buildingNumber;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    private UserAddress(
            final Long id,
            final Long userId,
            final String address,
            final String subAddress,
            final String buildingNumber,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {

        this.id = id;
        this.userId = userId;
        this.address = address;
        this.subAddress = subAddress;
        this.buildingNumber = buildingNumber;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    public static UserAddress of(
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new UserAddress(null, null, address, subAddress, buildingNumber, null, null);
    }

    public static UserAddress of(
            final Long id,
            final Long userId,
            final String address,
            final String subAddress,
            final String buildingNumber,
            final LocalDateTime createdAt,
            final LocalDateTime lastModified
    ) {
        return new UserAddress(id, userId, address, subAddress, buildingNumber, createdAt, lastModified);
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
