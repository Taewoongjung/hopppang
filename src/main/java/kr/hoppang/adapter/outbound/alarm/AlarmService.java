package kr.hoppang.adapter.outbound.alarm;

import kr.hoppang.adapter.outbound.alarm.dto.ErrorAlarm;
import kr.hoppang.adapter.outbound.alarm.dto.NewEstimation;
import kr.hoppang.adapter.outbound.alarm.dto.NewUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface AlarmService {

    void sendErrorAlarm(final ErrorAlarm event);

    void sendNewEstimationAlarm(final NewEstimation newEstimation);

    void sendNewUserAlarm(final NewUser newUser);
}
