package kr.hoppang.adapter.common.util;

import java.util.function.Predicate;
import kr.hoppang.adapter.common.exception.ErrorType;
import kr.hoppang.adapter.common.exception.custom.InvalidInputException;

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
}
