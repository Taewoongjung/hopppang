package kr.hoppang.application.readmodel.chassis.handlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import kr.hoppang.config.security.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@ActiveProfiles("dev")
public class JwtTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private WebClient webClient;

    @Test
    public void 토큰_생성_테스트() throws ParseException {

        String expireIn = "2024-10-24T19:49:44";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC 시간대 설정
        Date date = dateFormat.parse(expireIn);

        long timestamp = date.getTime();

        String aa = jwtUtil.createJwtForSso(
                "000231.cab8dfd62b7743b9a0f67348cc3922a0.1610",
                "CUSTOMER",
                "APL",
                new Date(timestamp)
        );

        System.out.println(aa);
    }
}
