package kr.hoppang.adapter.outbound.alarm.dto;

import java.time.LocalDateTime;
import kr.hoppang.domain.user.OauthType;

public record NewUser(String userName,
                      String userEmail,
                      OauthType oauthType,
                      LocalDateTime createdAt) {

}
