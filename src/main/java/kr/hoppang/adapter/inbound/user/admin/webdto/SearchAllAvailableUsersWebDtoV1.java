package kr.hoppang.adapter.inbound.user.admin.webdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.BindParam;

public record SearchAllAvailableUsersWebDtoV1() {

    public record Req(
            @NotNull
            @Min(
                    value = 0,
                    message = "offset는 {value} 미만일 수 없습니다."
            )
            @BindParam(value = "offset")
            Long offset,

            @NotNull
            @Range(
                    min = 1,
                    max = 1000,
                    message = "limit는 최소 {min}개 최대 {max}개를 초과할 수 없습니다."
            )
            @BindParam(value = "limit")
            Integer limit

    ) { }

    @Builder(access = AccessLevel.PRIVATE)
    public record Res(
            List<UserInfo> userList,
            long count
    ) {

        @Builder(access = AccessLevel.PRIVATE)
        private record UserInfo(
                long id,
                String name,
                String email,
                OauthType oauthType,
                LocalDateTime createdAt
        ) { }

        public static SearchAllAvailableUsersWebDtoV1.Res of(
                final List<User> userList,
                final long count
        ) {

            return Res.builder()
                    .userList(
                            Optional.ofNullable(userList)
                                    .map(users ->
                                            users.stream()
                                                    .map(
                                                            user ->
                                                                    UserInfo.builder()
                                                                            .id(user.getId())
                                                                            .name(user.getName())
                                                                            .email(user.getEmail())
                                                                            .oauthType(
                                                                                    user.getOauthType())
                                                                            .createdAt(
                                                                                    user.getCreatedAt())
                                                                            .build()
                                                    )
                                                    .toList()
                                    ).orElseGet(List::of)
                    )
                    .count(count)
                    .build();
        }
    }
}
