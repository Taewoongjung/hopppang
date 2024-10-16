package kr.hoppang.adapter.outbound.jpa.repository.sms;

import static kr.hoppang.util.converter.sms.SmsEntityConverter.smsSendResultToEntity;

import kr.hoppang.adapter.outbound.jpa.entity.sms.SmsSendResultEntity;
import kr.hoppang.domain.sms.SmsSendResult;
import kr.hoppang.domain.sms.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class SmsRepositoryAdapter implements SmsRepository {

    private final SmsJpaRepository smsJpaRepository;

    @Override
    @Transactional
    public void save(final SmsSendResult smsSendResult) {
        SmsSendResultEntity entity = smsSendResultToEntity(smsSendResult);
        smsJpaRepository.save(entity);
    }
}
