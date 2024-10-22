package kr.hoppang.application.command.user.oauth.dto;

public record TokenResponse(String access_token,
                            String token_type,
                            Long expires_in,
                            String refresh_token,
                            String id_token) {

}
