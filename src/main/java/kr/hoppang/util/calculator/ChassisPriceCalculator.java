package kr.hoppang.util.calculator;

import java.math.BigDecimal;
import java.math.MathContext;

public class ChassisPriceCalculator {

    private final static int valueOf10X10 = 100; // 10 X 10 에 대한 값

    public static int calculate(
            final int priceFromDBOfWidthAndHeight,
            final int estimateWidth, // 실제 인입 된 너비에 대한 근사치
            final int estimateHeight, // 실제 인입 된 높이에 대한 근사치
            final int width, // 실제 인입 된 너비
            final int height // 실제 인입 된 높이
    ) {

        // 근사치에 대한 너비 (estimateWidth) 높이 (estimateHeight)에 대한 10X10을 구한다.
        BigDecimal approxDimension = BigDecimal.valueOf((long) estimateWidth * estimateHeight / valueOf10X10);
        BigDecimal approxPriceOf10X10 = BigDecimal.valueOf(priceFromDBOfWidthAndHeight).divide(approxDimension, MathContext.DECIMAL32);

        // 실제 인입에 대한 너비 (width) 높이 (height)에 대한 넓이를 구한다.
        BigDecimal dimension = BigDecimal.valueOf((long) width * height).divide(BigDecimal.valueOf(valueOf10X10), MathContext.DECIMAL32);

        return dimension.multiply(approxPriceOf10X10, MathContext.UNLIMITED).intValue();
    }
}
