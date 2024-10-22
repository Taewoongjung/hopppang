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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hoppang.adapter.outbound.jpa.entity.BaseEntity;
import kr.hoppang.domain.user.UserAddress;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "user_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddressEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    private String address;

    private String subAddress;

    private String buildingNumber;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    private UserAddressEntity(
            final Long id,
            final Long userId,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {

        super(LocalDateTime.now(), LocalDateTime.now());

        this.id = id;
        this.userId = userId;
        this.address = address;
        this.subAddress = subAddress;
        this.buildingNumber = buildingNumber;
    }

    public static UserAddressEntity of(
            final Long userId,
            final String address,
            final String subAddress,
            final String buildingNumber
    ) {
        return new UserAddressEntity(null, userId, address, subAddress, buildingNumber);
    }

    public UserAddress toPojo() {
        return UserAddress.of(
                getId(),
                getUserId(),
                getAddress(),
                getSubAddress(),
                getBuildingNumber(),
                getCreatedAt(),
                getLastModified()
        );
    }
}
