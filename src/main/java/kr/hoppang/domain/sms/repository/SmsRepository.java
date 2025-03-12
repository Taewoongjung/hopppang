package kr.hoppang.domain.sms.repository;

import kr.hoppang.domain.sms.SmsSendResult;

public interface SmsRepository {

    void createSmsResult(final SmsSendResult smsSendResult);
}
