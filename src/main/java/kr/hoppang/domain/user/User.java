package kr.hoppang.domain.user;

import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_ACCESS_TOKEN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_REFRESH_TOKEN;
import static kr.hoppang.adapter.common.exception.ErrorType.NO_HISTORY_PUBLISHED_REFRESH_TOKEN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends Throwable implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String tel;
    private UserRole userRole;
    private OauthType oauthType;
    private String deviceId;

    private List<UserToken> userTokenList = new ArrayList<>();

    private UserAddress userAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModified;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;


    private User(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType,
            final String deviceId,
            final List<UserToken> userTokenList,
            final UserAddress userAddress,
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.userRole = userRole;
        this.oauthType = oauthType;
        this.deviceId = deviceId;
        this.userTokenList = userTokenList;
        this.userAddress = userAddress;
        this.lastModified = lastModified;
        this.createdAt = createdAt;
    }

    public static User of(
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType,
            final String deviceId,
            final List<UserToken> userTokenList,
            final UserAddress userAddress,
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {

        return new User(null, name, email, password,
                tel, userRole, oauthType, deviceId,
                userTokenList, userAddress, lastModified, createdAt);
    }

    public static User of(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final OauthType oauthType,
            final String deviceId,
            final List<UserToken> userTokenList,
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {

        return new User(id, name, email, password, tel, userRole, oauthType,
                deviceId, userTokenList, null, lastModified, createdAt);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getMaskedEmail() {
        if (this.email == null || !this.email.contains("@")) {
            return this.email; // 유효한 이메일이 아니면 그대로 반환
        }

        String[] parts = this.email.split("@");
        if (parts[0].length() <= 2) {
            return this.email; // '@' 앞 부분이 두 글자 이하인 경우 그대로 반환
        }

        Pattern pattern = Pattern.compile("(^..)[^@]*(?=@)");
        Matcher matcher = pattern.matcher(this.email);

        if (matcher.find()) {
            String firstTwoChars = matcher.group(1);
            int starsCount = matcher.group().length() - 2;
            String stars = "*".repeat(starsCount);
            return matcher.replaceFirst(firstTwoChars + stars);
        }
        
        return this.email;
    }

    public boolean isLatestRefreshTokenValid() {
        List<UserToken> orderReversedUserTokenList = this.getUserTokenList().stream()
                .sorted(Comparator.comparing(UserToken::getCreatedAt).reversed())
                .toList();

        UserToken userToken = orderReversedUserTokenList.stream()
                .filter(f -> TokenType.REFRESH.equals(f.getTokenType()))
                .findFirst().orElseThrow(() -> new HoppangLoginException(NO_HISTORY_PUBLISHED_REFRESH_TOKEN));

        return userToken.getExpireIn().isBefore(LocalDateTime.now());
    }

    public String getTheLatestRefreshToken() {
        List<UserToken> orderReversedUserTokenList = this.getUserTokenList().stream()
                .sorted(Comparator.comparing(UserToken::getCreatedAt).reversed())
                .toList();

        UserToken userToken = orderReversedUserTokenList.stream()
                .filter(f -> TokenType.REFRESH.equals(f.getTokenType()))
                .findFirst().orElseThrow(() -> new HoppangLoginException(NOT_EXIST_REFRESH_TOKEN));

        return userToken.getToken();
    }

    public void reviseTheLatestAccessToken(
            final String accessToken,
            final LocalDateTime accessTokenExpireInLocalDateTime
    ) {
        UserToken userToken = this.getUserTokenList().stream()
                .filter(f -> TokenType.ACCESS.equals(f.getTokenType()))
                .findFirst().orElseThrow(() -> new HoppangLoginException(NOT_EXIST_ACCESS_TOKEN));

        userToken.reviseToken(accessToken, accessTokenExpireInLocalDateTime);
    }
}