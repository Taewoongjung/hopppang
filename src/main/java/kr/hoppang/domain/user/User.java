package kr.hoppang.domain.user;

import static kr.hoppang.adapter.common.util.CheckUtil.require;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.userRole = userRole;
        this.lastModified = lastModified;
        this.createdAt = createdAt;
    }

    public static User of(
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {

        return new User(null, name, email, password, tel, userRole, lastModified, createdAt);
    }

    public static User of(
            final Long id,
            final String name,
            final String email,
            final String password,
            final String tel,
            final UserRole userRole,
            final LocalDateTime lastModified,
            final LocalDateTime createdAt
    ) {

        return new User(id, name, email, password, tel, userRole, lastModified, createdAt);
    }

    public static User of(
            final String email,
            final String userRole
    ) {

        return new User(null, null, email, null, null, UserRole.from(userRole), null, null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("CUSTOMER"));
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
}