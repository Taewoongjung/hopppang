package kr.hoppang.application.readmodel.chassis.handlers;

import static kr.hoppang.util.calculator.ApproximateCalculator.getApproximateHeight;
import static kr.hoppang.util.calculator.ApproximateCalculator.getApproximateWidth;

import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import org.junit.jupiter.api.Test;

class CalculateChassisPriceQueryHandlerTest {

        void IMPORTANT_NOTE() {
            // 제작불가 크기
            //      - 너비 : 300 미만, 5000 초과
            //      - 높이 : 300 미만, 2600 초과

            // ## 예제 1
            int originPriceOfTheChassisFromDB1 = 1475600;
            ChassisType chassisType1 = ChassisType.BalconySingle; // 발코니단창
            CompanyType companyType1 = CompanyType.HYUNDAI;
            int width1 = 3650;
            int height1 = 2350;
            int floorCustomerLiving1 = 15;
            boolean isScheduledForDemolition1 = true;

            System.out.println("width = " + getApproximateWidth(width1));
            System.out.println("height = " + getApproximateHeight(height1));

            // # 예제 1 풀이
            //      디비에 가로 3600 세로 2400 => 1475600 (디비에 있는 값)
            //      이거에 대한 10 X 10을 구한다. => 3600 * 2400 / 100 = 86400
            //      10 * 10 에 대한 값을 구한다. => 1475600 / 86400 = 17.08 (두자리수 부터 올림)
            //      3650 * 2350 에 대한 10 * 10 을 구한다. => 3650 * 2350 / 100 = 85775
            //      85775 * 17.08 을 하면 된다. (올림)


            // ## 예제 2
            int originPriceOfTheChassisFromDB2 = 1119500;
            ChassisType chassisType2 = ChassisType.LivingRoomSliding; // 거실분합창
            CompanyType companyType2 = CompanyType.HYUNDAI;
            int width2 = 3050;
            int height2 = 2460;
            int floorCustomerLiving2 = 15;
            boolean isScheduledForDemolition2 = true;

            System.out.println("width = " + getApproximateWidth(width2));
            System.out.println("height = " + getApproximateHeight(height2));

            // # 예제 2 풀이
            //      디비에 가로 3000  세로 2400 => 1119500
            //      이거에 대한 10 * 10을 구한다. => 3000 * 2400 / 100 = 72000 (여기서 100 의 의미는 10 * 10 임)
            //      10 * 10 에 대한 값을 구한다. => 1119500 / 72000 = 15.55
            //      3050 * 2460 에 대한 10 * 10 을 구한다. => 3050 * 2460 / 100 = 75030
            //      75030 * 15.55 = 1166717


            // ## 인건비
                // 단창 3600 / 이중창 4600
                // 단창/이중창 선택 후 "(가로/300) * (세로/300) = 평수" 를 구한 다음, 거기에 단창이면 3600을 이중창이면 4600을 곱한다.
                // 여기에서 기본 인건비 480000원이 안넘으면 그 차액을 자재비에 더한다.
                // 만약 480000원이 넘으면 그냥 도출 된 금액을 자재비에 더한다

            // # 사다리차

            // # 철거 유무
            //      철거비 40만원 측정하기로 함.

            // # 비어있을 때 / 살고있을 때
            //      살고 있을 때는 25만원 추가 (보양비)


            // ## 가변 변수 리스트
            //      o 철거비
            //      o 보양비
            //      o 최소 인건비
    }
}