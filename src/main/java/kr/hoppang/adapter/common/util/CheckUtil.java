package kr.hoppang.adapter.common.util;

import java.util.function.Predicate;
import kr.hoppang.adapter.common.exception.ErrorType;
import kr.hoppang.adapter.common.exception.custom.HoppangDuplicatedLoginException;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.adapter.common.exception.custom.InvalidInputException;
import kr.hoppang.domain.user.OauthType;

public class CheckUtil {
    public static <T> void require(final Predicate<T> predicate, final T target, final ErrorType errorType) {
        if (predicate.test(target)) {
            throw new InvalidInputException(errorType);
        }
    }

    public static void check(final boolean condition, final ErrorType errorType) {
        if (condition) {
            throw new InvalidInputException(errorType);
        }
    }

    public static void loginCheck(final boolean condition, final ErrorType errorType) {
        if (condition) {
            throw new HoppangLoginException(errorType);
        }
    }

    public static void duplicatedSsoLoginCheck(final boolean condition, final String email,
            final OauthType oauthType) {
        if (condition) {
            throw new HoppangDuplicatedLoginException(email, oauthType);
        }
    }
}
