package kr.hoppang.adapter.outbound.alarm;

import kr.hoppang.adapter.outbound.alarm.dto.ErrorAlarm;
import kr.hoppang.adapter.outbound.alarm.dto.NewEstimation;

public interface AlarmService {

    void sendErrorAlarm(final ErrorAlarm event);

    void sendNewEstimation(final NewEstimation newEstimation);
}
