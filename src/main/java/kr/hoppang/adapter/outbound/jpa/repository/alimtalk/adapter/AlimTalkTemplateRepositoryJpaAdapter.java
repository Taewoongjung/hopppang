package kr.hoppang.adapter.outbound.jpa.repository.alimtalk.adapter;

import static kr.hoppang.adapter.common.exception.ErrorType.NO_ALIM_TALK_TEMPLATE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.adapter.outbound.jpa.entity.alimtalk.AlimTalkTemplateEntity;
import kr.hoppang.adapter.outbound.jpa.repository.alimtalk.AlimTalkTemplateJpaRepository;
import kr.hoppang.domain.alimtalk.AlimTalkTemplate;
import kr.hoppang.domain.alimtalk.AlimTalkTemplateType;
import kr.hoppang.domain.alimtalk.AlimTalkThirdPartyType;
import kr.hoppang.domain.alimtalk.repository.AlimTalkTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlimTalkTemplateRepositoryJpaAdapter implements AlimTalkTemplateRepository {

    private final AlimTalkTemplateJpaRepository alimTalkTemplateJpaRepository;


    @Override
    public AlimTalkTemplate findByTemplateType(final AlimTalkTemplateType templateType) {

        AlimTalkTemplateEntity templateEntity = alimTalkTemplateJpaRepository.findByTypeAndThirdPartyType(
                templateType,
                AlimTalkThirdPartyType.ALIGO
        );

        check(templateEntity == null, NO_ALIM_TALK_TEMPLATE);

        return templateEntity.toPojo();
    }
}
