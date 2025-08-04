package kr.hoppang.domain.alimtalk.repository;

import kr.hoppang.domain.alimtalk.AlimTalkTemplate;
import kr.hoppang.domain.alimtalk.AlimTalkTemplateType;

public interface AlimTalkTemplateRepository {

    AlimTalkTemplate findByTemplateType(AlimTalkTemplateType templateCode);
}
