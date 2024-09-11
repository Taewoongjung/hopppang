package kr.hoppang.util.calculator;

import java.util.Arrays;
import java.util.List;

// 이 클래스에서는 너비/높이가 인입 되면 실제 가격 데이터가 측정 되어 있는 디비의 데이터의 너비/높이 에 비교 해서 근사치를 뽑는다.
public class ApproximateCalculator {

    final static List<Integer> widthList = Arrays.asList( 300, 600, 900, 1200, 1500, 1800, 2100, 2400, 2700, 3000, 3300, 3600, 3900, 4200, 4500, 4800, 5100, 5400 );
    final static List<Integer> heightList = Arrays.asList( 300, 600, 900, 1200, 1500, 1800, 2100, 2400, 2700 );

    public static int getApproximateWidth(final int targetWidth) {

        // @ 최소 단위 미달 : 300 보다 작으면 300 리턴
        if (300 > targetWidth) {
            return widthList.get(0);
        }

        // @ 최대 단위 초과 : 5400 보다 크면 5400 리턴
        if (5400 < targetWidth) {
            return widthList.get(widthList.size() - 1);
        }

        // targetWidth 보다 작고 보다 큰거 하나씩 고르기
        int targetLength = -1;
        for (int widthFromList : widthList) {

            int tmpResult = -1;
            tmpResult = targetWidth - widthFromList;
            if (tmpResult < 0) {
                targetLength = widthList.indexOf(widthFromList);
                break;
            }
        }

        int targetPrevWidth = widthList.get(targetLength - 1);
        int targetNextWidth = widthList.get(targetLength);

        // 바로 전 데이터와의 비교
        int comparePrevToTarget = Math.abs(targetWidth - targetPrevWidth);
        int compareNextToTarget = Math.abs(targetWidth - targetNextWidth);

        // 뺄셈의 결과가 적은 숫자가 가까운 숫자임. 그래서 그게 최종 width 가 됨
        if (compareNextToTarget > comparePrevToTarget) {
            return targetPrevWidth;
        } else {
            return targetNextWidth;
        }
    }

    public static int getApproximateHeight(final int targetHeight) {
        // @ 최소 단위 미달 : 300 보다 작으면 300 리턴
        if (300 > targetHeight) {
            return heightList.get(0);
        }

        // @ 최대 단위 초과 : 5400 보다 크면 5400 리턴
        if (5400 < targetHeight) {
            return heightList.get(heightList.size() - 1);
        }

        // targetHeight 보다 작고 보다 큰거 하나씩 고르기
        int targetLength = -1;
        for (int heightFromList : heightList) {

            int tmpResult = -1;
            tmpResult = targetHeight - heightFromList;
            if (tmpResult < 0) {
                targetLength = heightList.indexOf(heightFromList);
                break;
            }
        }

        int targetPrevHeight = heightList.get(targetLength - 1);
        int targetNextHeight = heightList.get(targetLength);

        // 바로 전 데이터와의 비교
        int comparePrevToTarget = Math.abs(targetHeight - targetPrevHeight);
        int compareNextToTarget = Math.abs(targetHeight - targetNextHeight);

        // 뺄셈의 결과가 적은 숫자가 가까운 숫자임. 그래서 그게 최종 width 가 됨
        if (compareNextToTarget > comparePrevToTarget) {
            return targetPrevHeight;
        } else {
            return targetNextHeight;
        }
    }
}
