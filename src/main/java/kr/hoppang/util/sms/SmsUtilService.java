package kr.hoppang.util.sms;

import kr.hoppang.abstraction.serviceutill.IThirdPartyValidationCheckSender;
import kr.hoppang.adapter.outbound.cache.sms.CacheSmsValidationRedisRepository;
import kr.hoppang.adapter.outbound.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("SmsUtilService")
public class SmsUtilService implements IThirdPartyValidationCheckSender {

    private final SmsService smsService;
    private final CacheSmsValidationRedisRepository cacheSmsValidationRedisRepository;

    @Override
    public void sendValidationCheck(final String targetPhoneNumber) throws Exception {

        int validationNumber = createNumber();
        cacheSmsValidationRedisRepository.makeBucketByKey(targetPhoneNumber, validationNumber);

        smsService.sendSignUpVerificationSms(targetPhoneNumber, String.valueOf(validationNumber));

        log.info("sent validation number is {}", validationNumber);
    }

    private int createNumber() {
        return (int) (Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }
}
