package kr.hoppang.adapter.template.sms;

import static kr.hoppang.adapter.common.exception.ErrorType.EMPTY_VALIDATION_NUMBER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import lombok.Getter;

@Getter
public class SmsTemplate {

    private final String textMessage;

    private SmsTemplate(final String textMessage) {
        this.textMessage = textMessage;
    }

    public static SmsTemplate phoneValidationTemplate(final String verificationNumber) {

        check(verificationNumber == null, EMPTY_VALIDATION_NUMBER);
        check("".equals(verificationNumber.trim()), EMPTY_VALIDATION_NUMBER);

        String msg =
                "인증번호 [" + verificationNumber + "]\n"
                        + "안녕하세요 '호빵' 입니다.\n"
                        + "\"타인 노출 금지\"";

        return new SmsTemplate(msg);
    }
}
