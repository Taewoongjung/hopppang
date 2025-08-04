package kr.hoppang.domain.alimtalk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlimTalkTemplateType {
    COMPLETE_SIGNUP("회원가입 완료", "UB_4026"),
    WINDOW_INQUIRY_REPLY("담당직원 연락 필요 여부 요청", "TY_9241");

    private final String description;
    private final String type;
}
