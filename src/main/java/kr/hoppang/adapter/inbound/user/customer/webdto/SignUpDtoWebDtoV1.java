package kr.hoppang.adapter.inbound.user.customer.webdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.UserRole;

public class SignUpDtoWebDtoV1 {

    public record Req(
            @NotNull
            String name,
            @NotNull
            String password,
            @NotNull
            @Email
            String email,
            @NotNull
            String tel,
            @NotNull
            UserRole role,
            @NotNull
            OauthType oauthType,
            String deviceId,
            String deviceType,
            String address,
            String subAddress,
            String buildingNumber,
            Boolean isPushOn
    ) {

    }

    public record Res(Boolean isSuccess, String SignedUpUserName) {

    }
}
