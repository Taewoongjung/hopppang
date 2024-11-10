package kr.hoppang.application.command.user.oauth.dto;

public record GoogleUserResource(String id,
                                String email,
                                String verified_email,
                                String picture) {

}
