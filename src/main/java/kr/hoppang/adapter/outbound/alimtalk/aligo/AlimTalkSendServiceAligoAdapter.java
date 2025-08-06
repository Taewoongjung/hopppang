package kr.hoppang.adapter.outbound.alimtalk.aligo;

import static kr.hoppang.adapter.common.exception.ErrorType.ALIM_TALK_SEND_ERROR;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hoppang.adapter.outbound.gson.CustomTypeAdapter;
import kr.hoppang.application.command.alimtalk.AlimTalkSendService;
import kr.hoppang.domain.alimtalk.AlimTalkButtonLinkType;
import kr.hoppang.domain.alimtalk.AlimTalkMessageType;
import kr.hoppang.domain.alimtalk.AlimTalkResult;
import kr.hoppang.domain.alimtalk.AlimTalkTemplate;
import kr.hoppang.domain.alimtalk.repository.AlimTalkResultRepository;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Primary
@Slf4j
@Service
@RequiredArgsConstructor
public class AlimTalkSendServiceAligoAdapter implements AlimTalkSendService {

    @Value("${kakao.aligo.key}")
    private String KEY;

    @Value("${kakao.aligo.user-id}")
    private String USER_ID;

    @Value("${kakao.aligo.sender-key}")
    private String SENDER_KEY;

    private final String senderPhoneNumber = "01029143611";

    private final WebClient webClient;
    private final AlimTalkResultRepository alimTalkResultRepository;

    public void syncTemplate() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("apikey", KEY);
        formData.add("userid", USER_ID);
        formData.add("senderkey", SENDER_KEY);

        Mono<String> responseMono = webClient.post()
                .uri("https://kakaoapi.aligo.in/akv10/template/list/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class);

        try {
            String response = responseMono.block(); // ← 동기 방식으로 변환
            List<Map<String, Object>> parsedRes = parseSuccessString(response);
            for (Map<String, Object> template : parsedRes) {
                log.info("templtName: {}", template.get("templtName"));
                log.info("templtCode: {}", template.get("templtCode"));
                log.info("templateExtra: {}", template.get("templateExtra"));
                log.info("templtTitle: {}", template.get("templtTitle"));
                log.info("templtContent: {}", template.get("templtContent"));
                log.info("buttons: {}", template.get("buttons"));
            }
        } catch (Exception e) {
            log.error("[Err_msg] 알림톡 템플릿 싱크 중 에러 발생: {}", e.getMessage(), e);
        }
    }

    private List<Map<String, Object>> parseSuccessString(final String target) {
        System.out.println("? = " + target);
        // Gson 인스턴스 생성
        Gson gson = new Gson();

        // JSON 문자열을 Map으로 변환
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> jsonMap = gson.fromJson(target, mapType);

        return (List<Map<String, Object>>) jsonMap.get("list");
    }


    @Override
    @Transactional
    public void send(
            final String receiverPhoneNumber,
            final AlimTalkTemplate alimTalkTemplate
    ) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("apikey", KEY);
        formData.add("userid", USER_ID);
        formData.add("senderkey", SENDER_KEY);
        formData.add("tpl_code", alimTalkTemplate.getTemplateCode());
        formData.add("subject_1", alimTalkTemplate.getMessageSubject());
        formData.add("message_1", alimTalkTemplate.getMessage());
        formData.add("sender", senderPhoneNumber);
        formData.add("receiver_1", receiverPhoneNumber);

        // 버튼 정보를 JSON 문자열로 구성하여 추가
        if (!AlimTalkButtonLinkType.NO.equals(alimTalkTemplate.getButtonLinkType())) {
            formData.add("button_1", alimTalkTemplate.getButtonInfo());
        }

        log.info("formData: {}", formData);

        Mono<String> responseMono;
        String response = null;
        try {
            responseMono = webClient.post()
                    .uri("https://kakaoapi.aligo.in/akv10/alimtalk/send/")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(String.class);

            response = responseMono.block();

        } catch (Exception e) {
            log.error("[Err_msg] 알림톡 전송 에러 (템플릿코드: {}) : {}", alimTalkTemplate.getTemplateCode(),
                    e.toString());
        }

        log.info("response: {}", response);

        if (response == null) {
            return;
        }

        Map<String, String> res = parseSendResult(response);

        log.info("res: {}", res);

        alimTalkResultRepository.save(
                AlimTalkResult.builder()
                        .templateCode(alimTalkTemplate.getTemplateCode())
                        .templateName(alimTalkTemplate.getTemplateName())
                        .messageSubject(alimTalkTemplate.getMessageSubject())
                        .message(alimTalkTemplate.getMessage())
                        .messageType(AlimTalkMessageType.KKO)
                        .receiverPhone(receiverPhoneNumber)
                        .msgId("0".equals(res.get("code")) ? res.get("mid") : null)
                        .isSuccess("0".equals(res.get("code")) ? BoolType.T : BoolType.F)
                        .build()
        );
    }

    private Map<String, String> parseSendResult(final String target) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, String>>() {
        }.getType(), new CustomTypeAdapter());
        Gson gson = gsonBuilder.create();

        // JSON 문자열을 Map으로 변환
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();

        Map<String, String> wholeJson = gson.fromJson(target, mapType);

        check("-99".equals(wholeJson.get("code")), ALIM_TALK_SEND_ERROR);

        Map<String, String> infoFromWholeJson = gson.fromJson(wholeJson.get("info"), mapType);

        Map<String, String> resultMap = new HashMap<>(wholeJson);
        resultMap.putAll(infoFromWholeJson);

        return resultMap;
    }
}
