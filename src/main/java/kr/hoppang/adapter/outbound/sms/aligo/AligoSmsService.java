package kr.hoppang.adapter.outbound.sms.aligo;

import static kr.hoppang.adapter.common.exception.ErrorType.UNABLE_TO_SEND_SMS;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import kr.hoppang.adapter.outbound.sms.SmsService;
import kr.hoppang.adapter.template.sms.SmsTemplate;
import kr.hoppang.application.command.log.sms.event.SmsSendResultLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AligoSmsService implements SmsService {

    private final WebClient webClient;
    private final ApplicationEventPublisher eventPublisher;

    private final String senderPhoneNumber = "01029143611";


    @Value("${sms.aligo.key}")
    private String KEY;

    @Value("${sms.aligo.user-id}")
    private String USER_ID;


    @Override
    public void sendSignUpVerificationSms(
            final String to,
            final String verificationNumber
    ) throws Exception {

        SmsTemplate template = SmsTemplate.phoneValidationTemplate(verificationNumber);

        try {

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("key", KEY);
            formData.add("user_id", USER_ID);
            formData.add("sender", senderPhoneNumber);
            formData.add("receiver", to);
            formData.add("msg", template.getTextMessage());

            Mono<String> responseMono = webClient.post()
                    .uri("https://apis.aligo.in/send/")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve().bodyToMono(String.class);

            responseMono.subscribe(response -> {

                HashMap<String, String> parsedRes = parseSuccessString(response);

                if (parsedRes != null) {

                    eventPublisher.publishEvent(new SmsSendResultLogEvent(
                            senderPhoneNumber,
                            to,
                            parsedRes,
                            "ALIGO"
                    ));
                }
            }, error -> {
                log.error("[Err_msg] 휴대폰 ({}) 인증 sms 발송 실패 = {}", to, error.toString());

                eventPublisher.publishEvent(new SmsSendResultLogEvent(
                        senderPhoneNumber,
                        to,
                        null,
                        "ALIGO"
                ));
            });

        } catch (Exception e) {
            log.error("[Err_msg] 휴대폰 ({}) 인증 sms 발송 실패 = {}", to, e.toString());
            eventPublisher.publishEvent(new SmsSendResultLogEvent(
                    senderPhoneNumber,
                    to,
                    null,
                    "ALIGO"
            ));
            throw new Exception(UNABLE_TO_SEND_SMS.getMessage());
        }
    }

    private HashMap<String, String> parseSuccessString(final String target) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(target, new TypeReference<>() {
            });

        } catch (Exception e) {
            log.error("[Err_msg] 로그 생성 전 파싱 에러 = {}", e.toString());
        }

        return null;
    }
}
