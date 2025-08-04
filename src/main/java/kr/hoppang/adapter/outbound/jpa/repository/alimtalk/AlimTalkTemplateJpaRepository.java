package kr.hoppang.adapter.outbound.jpa.repository.alimtalk;

import kr.hoppang.adapter.outbound.jpa.entity.alimtalk.AlimTalkTemplateEntity;
import kr.hoppang.domain.alimtalk.AlimTalkTemplateType;
import kr.hoppang.domain.alimtalk.AlimTalkThirdPartyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimTalkTemplateJpaRepository extends
        JpaRepository<AlimTalkTemplateEntity, Long> {

    AlimTalkTemplateEntity findByTypeAndThirdPartyType(AlimTalkTemplateType type, AlimTalkThirdPartyType thirdPartyType);
}
