package kr.hoppang.domain.chassis;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
    LX ("LX 하우시스"),
    HYUNDAI ("현대 L&C"),
    KCC_GLASS ("KCC 글라스");

    private final String companyName;

    public static CompanyType from(final String target) {
        return Arrays.stream(values())
                .filter(f->target.equals(f.name()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("CompanyType 에 없는 필드 입니다."));
    }
}
