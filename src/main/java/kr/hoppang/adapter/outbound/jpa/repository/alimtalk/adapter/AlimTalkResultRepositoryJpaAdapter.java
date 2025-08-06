package kr.hoppang.adapter.outbound.jpa.repository.alimtalk.adapter;

import static kr.hoppang.util.converter.alimtalk.AlimTalkResultConverter.toEntity;

import kr.hoppang.adapter.outbound.jpa.repository.alimtalk.AlimTalkResultJpaRepository;
import kr.hoppang.domain.alimtalk.AlimTalkResult;
import kr.hoppang.domain.alimtalk.repository.AlimTalkResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class AlimTalkResultRepositoryJpaAdapter implements AlimTalkResultRepository {

    private final AlimTalkResultJpaRepository alimTalkResultJpaRepository;


    @Override
    @Transactional
    public void save(final AlimTalkResult alimTalkResult) {

        log.info("save alimtalk result : {}", alimTalkResult);

        alimTalkResultJpaRepository.save(toEntity(alimTalkResult));
    }
}
