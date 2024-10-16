package kr.hoppang.adapter.outbound.jpa.repository.sms;

import kr.hoppang.adapter.outbound.jpa.entity.sms.SmsSendResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsJpaRepository extends JpaRepository<SmsSendResultEntity, Long> {

}
