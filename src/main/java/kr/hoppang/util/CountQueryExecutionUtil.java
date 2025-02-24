package kr.hoppang.util;

import java.util.List;
import java.util.function.LongSupplier;

public final class CountQueryExecutionUtil {

    public static <T> long count(
            List<T> contents,
            long offset,
            int limit,
            LongSupplier countSupplier
    ) {
        if (offset == 0) {
            return limit > contents.size() ? contents.size() : countSupplier.getAsLong();
        }

        if (!contents.isEmpty() && limit > contents.size()) {
            return offset + contents.size();
        }

        return countSupplier.getAsLong();
    }

}
