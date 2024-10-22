package kr.hoppang.application.command.user.oauth.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class AppleIDTokenPayload {

    private String iss;
    private String aud;
    private Long exp;
    private Long iat;
    private String sub;// users unique id
    private String at_hash;
    private Long auth_time;
    private Boolean nonce_supported;
    private Boolean email_verified;
    private String email;
}
