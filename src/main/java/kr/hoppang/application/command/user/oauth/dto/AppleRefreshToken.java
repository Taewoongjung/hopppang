package kr.hoppang.application.command.user.oauth.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class AppleRefreshToken {

    private String access_token;
    private String token_type;
    private Long expire_in;
}
