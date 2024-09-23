package kr.hoppang.domain.chassis;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum CompanyType {
    LX, HYUNDAI, KCC_GLASS;

    public static CompanyType from(final String target) {
        return Arrays.stream(values())
                .filter(f->target.equals(f.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("CompanyType 에 없는 필드 입니다."));
    }
}
