package kr.hoppang.util.converter.user;

import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.domain.user.User;

public class UserEntityConverter {

    public static UserEntity userToEntity(final User user) {

        return UserEntity.of(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getTel(),
                user.getUserRole(),
                user.getOauthType()
        );
    }
}
