package kr.hoppang.application.command.user.oauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleUserResource(
        @JsonProperty("id") String id,
        @JsonProperty("email") String email,
        @JsonProperty("verified_email") boolean verifiedEmail,
        @JsonProperty("picture") String picture,
        @JsonProperty("hd") String hd
) {}
