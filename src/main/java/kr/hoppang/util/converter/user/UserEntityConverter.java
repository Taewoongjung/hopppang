package kr.hoppang.util.converter.user;

import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.adapter.outbound.jpa.entity.user.UserTokenEntity;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserToken;

public class UserEntityConverter {

    public static UserEntity userToEntity(final User user) {

        return UserEntity.of(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getTel(),
                user.getUserRole(),
                user.getOauthType(),
                user.getDeviceId()
        );
    }

    public static UserEntity userToEntityWithRelations(final User user) {

        return UserEntity.of(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getTel(),
                user.getUserRole(),
                user.getOauthType(),
                user.getDeviceId(),
                user.getUserTokenList().stream()
                        .map(UserEntityConverter::userTokenToEntity)
                        .collect(Collectors.toList())
        );
    }

    private static UserTokenEntity userTokenToEntity(final UserToken userToken) {
        return UserTokenEntity.of(
                userToken.getProviderUserId(),
                userToken.getTokenType(),
                userToken.getToken(),
                userToken.getConnectedAt(),
                userToken.getExpireIn());
    }

    public static UserTokenEntity userTokenToEntity(final Long userId, final UserToken userToken) {
        return UserTokenEntity.of(
                userId,
                userToken.getProviderUserId(),
                userToken.getTokenType(),
                userToken.getToken(),
                userToken.getConnectedAt(),
                userToken.getExpireIn());
    }
}
