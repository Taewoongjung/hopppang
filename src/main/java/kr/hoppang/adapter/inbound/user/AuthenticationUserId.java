package kr.hoppang.adapter.inbound.user;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Retention(value = RUNTIME)
@Target(value = PARAMETER)
@AuthenticationPrincipal(expression = "id")
public @interface AuthenticationUserId {

}
