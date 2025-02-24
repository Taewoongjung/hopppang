package kr.hoppang.adapter.inbound.user.admin.webdto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;

public record SearchAllAvailableUsersWebDtoV1() {

    public record Response(
            List<UserInfo> userList
    ) {

        @Builder(access = AccessLevel.PRIVATE)
        private record UserInfo(
                long id,
                String name,
                String email,
                OauthType oauthType,
                LocalDateTime createdAt
        ) { }

        public static SearchAllAvailableUsersWebDtoV1.Response of(final List<User> userList) {

            return new SearchAllAvailableUsersWebDtoV1.Response(
                    Optional.ofNullable(userList)
                            .map(users ->
                                    users.stream()
                                            .map(
                                                    user ->
                                                            SearchAllAvailableUsersWebDtoV1.Response.UserInfo.builder()
                                                                    .id(user.getId())
                                                                    .name(user.getName())
                                                                    .email(user.getEmail())
                                                                    .oauthType(user.getOauthType())
                                                                    .createdAt(user.getCreatedAt())
                                                                    .build()
                                            )
                                            .toList()
                            ).orElseGet(List::of)
            );
        }
    }
}
